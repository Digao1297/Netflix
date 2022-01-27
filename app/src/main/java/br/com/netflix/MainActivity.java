package br.com.netflix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.netflix.model.Category;
import br.com.netflix.model.Movie;
import br.com.netflix.util.CategoryTask;

public class MainActivity extends AppCompatActivity implements CategoryTask.CategoryLoader {

    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _recycleViewInit();

        CategoryTask categoryTask = new CategoryTask(this);
        categoryTask.execute("https://tiagoaguiar.co/api/netflix/home");
        categoryTask.setCategoryLoader(this);
    }

    private void _recycleViewInit() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view_main);
        List<Category> categories = new ArrayList<>();

         this.mainAdapter = new MainAdapter(categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(this.mainAdapter);

    }

    @Override
    public void onRsult(List<Category> categories) {
        this.mainAdapter.setCategories(categories);
        this.mainAdapter.notifyDataSetChanged();
    }

    private static class CategoryHolder extends RecyclerView.ViewHolder {

        private final TextView textViewTitle;
        private final RecyclerView recyclerViewMovies;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            recyclerViewMovies = itemView.findViewById(R.id.recycler_view_movie);
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private List<Category> categories;

        public MainAdapter(List<Category> categories) {
            this.categories = categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories.clear();
            this.categories.addAll(categories);
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.textViewTitle.setText(category.getTitle());
            holder.recyclerViewMovies.setAdapter(new MovieAdapter(category.getMovies()));
            holder.recyclerViewMovies.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }
    }


    private static class MovieHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewCover;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCover = itemView.findViewById(R.id.image_view_cover);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        public MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
//            holder.imageViewCover.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }
}