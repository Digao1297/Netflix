package br.com.netflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.netflix.model.Movie;
import br.com.netflix.model.MovieDetail;
import br.com.netflix.util.ImageDownloaderTask;
import br.com.netflix.util.MovieDetailTask;

public class MovieInfoActivity extends AppCompatActivity implements MovieDetailTask.MovieDetailLoader {

    private TextView textViewTitle;
    private TextView textViewDesc;
    private TextView textViewCast;
    private ImageView coverImage;
    private ImageView playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        textViewTitle = findViewById(R.id.movie_info_title);
        textViewDesc = findViewById(R.id.movie_info_desc);
        textViewCast = findViewById(R.id.movie_info_cast);
        coverImage = findViewById(R.id.movie_info_cover);
        playButton = findViewById(R.id.movie_info_play_button);


        Toolbar toolbar = findViewById(R.id.movie_info_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");
            MovieDetailTask movieDetailTask = new MovieDetailTask(this);
            movieDetailTask.setMovieDetailLoader(this);
            movieDetailTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();


        return super.onOptionsItemSelected(item);
    }

    void setMovie(Movie movie) {
        textViewTitle.setText(movie.getTitle());
        textViewDesc.setText(movie.getDesc());
        textViewCast.setText(getString(R.string.cast, movie.getCast()));

        ProgressBar progressBar = findViewById(R.id.movie_info_progress);
        ImageDownloaderTask imageDownloaderTask = new ImageDownloaderTask(progressBar, coverImage, playButton, true);

        imageDownloaderTask.execute(movie.getCoverUrl());


    }

    void initRecycleView(List<Movie> movies) {
        RecyclerView recyclerView = findViewById(R.id.movie_info_recycleview);
        recyclerView.setAdapter(new MovieSimilarAdapter(movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

    }

    @Override
    public void onResult(MovieDetail movieDetail) {
        setMovie(movieDetail.getMovie());
        initRecycleView(movieDetail.getMoviesSimilar());
    }

    private static class MovieSimilarHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewCover;
        private final ProgressBar progressBar;


        public MovieSimilarHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
            progressBar = itemView.findViewById(R.id.movie_info_progress);
        }
    }

    private class MovieSimilarAdapter extends RecyclerView.Adapter<MovieSimilarHolder> {

        private final List<Movie> movies;

        public MovieSimilarAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieSimilarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieSimilarHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieSimilarHolder holder, int position) {
            Movie movie = movies.get(position);
            new ImageDownloaderTask(holder.progressBar, holder.imageViewCover, null, false).execute(movie.getCoverUrl());

        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}