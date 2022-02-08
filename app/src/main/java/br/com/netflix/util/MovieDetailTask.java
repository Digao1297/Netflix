package br.com.netflix.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import br.com.netflix.model.Movie;
import br.com.netflix.model.MovieDetail;

public class MovieDetailTask extends AsyncTask<String, Void, MovieDetail> {

    private final WeakReference<Context> contextWeakReference;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    public MovieDetailTask(Context context) {
        this.contextWeakReference = new WeakReference<>(context);
    }

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.contextWeakReference.get();

        if (context != null)
            dialog = ProgressDialog.show(context, "Carregando", "", true);

    }

    @Override
    protected MovieDetail doInBackground(String... params) {
        String url = params[0];
        try {
            URL requestUrl = new URL(url);

            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode > 400) {
                throw new IOException("Server communication error!");
            }

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String jsonAsString = toString(in);

            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));

            in.close();

            return movieDetail;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String toString(InputStream is) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;

        while ((read = is.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, read);
        }
        return byteArrayOutputStream.toString();
    }

    private MovieDetail getMovieDetail(JSONObject json) throws JSONException {
        int id = json.getInt("id");
        String title = json.getString("title");
        String desc = json.getString("desc");
        String cast = json.getString("cast");
        String coverUrl = json.getString("cover_url");

        List<Movie> movies = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("movie");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            movies.add(new Movie(
                    jsonObject.getInt("id"),
                    jsonObject.getString("cover_url")
            ));
        }
        return new MovieDetail(
                id, coverUrl, title, desc, cast,
                movies
        );
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        dialog.dismiss();

        if (movieDetailLoader != null)
            movieDetailLoader.onResult(movieDetail);
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
    }


}
