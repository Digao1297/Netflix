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

import br.com.netflix.model.Category;
import br.com.netflix.model.Movie;

public class CategoryTask extends AsyncTask<String, Void, List<Category>> {

    private ProgressDialog dialog;
    private final WeakReference<Context> context;
    private CategoryLoader categoryLoader;

    public CategoryTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void setCategoryLoader(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context context = this.context.get();

        if (context != null) {
            dialog = ProgressDialog.show(context, "Loading", "", true);
        }
    }

    @Override
    protected List<Category> doInBackground(String... params) {
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

            List<Category> categories = getCategories(new JSONObject(jsonAsString));

            in.close();

            return categories;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();

        if(categoryLoader != null)
            categoryLoader.onRsult(categories);
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

    private List<Category> getCategories(JSONObject json) throws JSONException {
        List<Category> categories = new ArrayList<>();
        List<Movie> movies = new ArrayList<>();

        JSONArray categoryArray = json.getJSONArray("category");
        for (int i = 0; i < categoryArray.length(); i++) {
            JSONObject categoryJson = categoryArray.getJSONObject(i);
            String title = categoryJson.getString("title");

            JSONArray movieArray = categoryJson.getJSONArray("movie");
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movieJson = movieArray.getJSONObject(j);

                Movie movie = new Movie(movieJson.getString("cover_url"));
                movies.add(movie);
            }
            Category category = new Category(title, movies);
            categories.add(category);
        }

        return categories;
    }

    public interface CategoryLoader {
        void onRsult(List<Category> categories);
    }
}
