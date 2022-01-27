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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.netflix.model.Movie;

public class MovieInfoActivity extends AppCompatActivity {

    private TextView textViewTitle;
    private TextView textViewDesc;
    private TextView textViewCast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        textViewTitle = findViewById(R.id.movie_info_title);
        textViewDesc = findViewById(R.id.movie_info_desc);
        textViewCast = findViewById(R.id.movie_info_cast);

        Toolbar toolbar = findViewById(R.id.movie_info_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
            getSupportActionBar().setTitle(null);
        }

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

        if (drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie_4);
            drawable.setDrawableByLayerId(R.id.shadows_cover_drawble, movieCover);
            ((ImageView) findViewById(R.id.movie_info_cover)).setImageDrawable(drawable);
        }

        textViewTitle.setText("Batman Begins");
        textViewDesc.setText("O jovem Bruce Wayne viaja para o Extremo Oriente, onde recebe treinamento em artes marciais do mestre Henri Ducard, um membro da misteriosa Liga das Sombras.");
        textViewCast.setText(getString(R.string.cast, ("Christian Bale "+",Michael Caine "+",Liam Neeson")));

        initRecycleView();
    }

    void initRecycleView(){
        List<Movie> movies = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            movies.add(new Movie(R.drawable.movie));
//        }
        RecyclerView recyclerView = findViewById(R.id.movie_info_recycleview);
        recyclerView.setAdapter(new MovieSimilarAdapter(movies));
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

    }

    private static class MovieSimilarHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewCover;

        public MovieSimilarHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
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
//            holder.imageViewCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}