package br.com.netflix.kotlin

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.netflix.R
import br.com.netflix.model.Movie
import br.com.netflix.model.MovieDetail
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_movie_info.*
import kotlinx.android.synthetic.main.activity_movie_info.view.*
import kotlinx.android.synthetic.main.movie_item_similar.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieInfoActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)
        initToolbar()
        setMovieDetail(intent.extras, this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() = with(movie_info_toolbar) {
        setSupportActionBar(movie_info_toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
            it.title = null
        }
    }

    private fun setMovieDetail(extras: Bundle?, context: Context) {
        extras?.let { ex ->
            val id: Int = ex.getInt("id")

            retrofit().create(NetflixAPI::class.java)
                .getMovieBy(id)
                .enqueue(object : Callback<MovieDetail> {

                    override fun onResponse(
                        call: Call<MovieDetail>,
                        response: Response<MovieDetail>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                movie_info_title.text = it.title
                                movie_info_desc.text = it.desc
                                movie_info_cast.text = it.cast

                                val movieSimilarAdapter =
                                    MovieSimilarAdapter(it.moviesSimilar)
                                movie_info_recycleview.adapter = movieSimilarAdapter
                                movie_info_recycleview.layoutManager =
                                    GridLayoutManager(this@MovieInfoActivity, 3)

                                Glide.with(context)
                                    .load(it.coverUrl)
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            return true
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable?,
                                            model: Any?,
                                            target: Target<Drawable>?,
                                            dataSource: DataSource?,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            val drawable: LayerDrawable =
                                                ContextCompat.getDrawable(
                                                    baseContext,
                                                    R.drawable.shadows
                                                ) as LayerDrawable

                                            drawable.setDrawableByLayerId(
                                                R.id.shadows_cover_drawble,
                                                resource
                                            )
                                            (target as DrawableImageViewTarget).view.setImageDrawable(
                                                drawable
                                            )

                                            return true
                                        }

                                    })
                                    .into(movie_info_cover)
                            }
                        }
                    }

                    override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                        Toast.makeText(this@MovieInfoActivity, t.message, Toast.LENGTH_SHORT).show()
                    }

                })


        }
    }


    private class MovieSimilarHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) = with(itemView) {
//            ImageDownloaderTask(
//                movie_similar_progress,
//                movie_similar_cover,
//                null,
//                false
//            ).execute(
//                movie.coverUrl
//            )

            Glide.with(context)
                .load(movie.coverUrl)
                .placeholder(R.drawable.placeholder_bg)
                .into(movie_similar_cover)
        }
    }

    private inner class MovieSimilarAdapter(val movies: List<Movie>) :
        RecyclerView.Adapter<MovieSimilarHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieSimilarHolder =
            MovieSimilarHolder(
                layoutInflater.inflate(
                    R.layout.movie_item_similar,
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: MovieSimilarHolder, position: Int) {
            holder.bind(movies[position])
        }

        override fun getItemCount(): Int = movies.size

    }


}