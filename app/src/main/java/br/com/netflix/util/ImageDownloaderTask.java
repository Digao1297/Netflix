package br.com.netflix.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import br.com.netflix.R;

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewWeakReference;
    private final ProgressBar progressBar;
    private final WeakReference<ImageView> playButton;
    private final boolean shadowEnabled;

    public ImageDownloaderTask(ProgressBar progressBar,
                               ImageView imageView,
                               ImageView playButton,
                               boolean shadowEnabled) {
        this.progressBar = progressBar;
        this.imageViewWeakReference = new WeakReference<>(imageView);
        this.playButton = new WeakReference<>(playButton);
        this.shadowEnabled = shadowEnabled;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressBar.setVisibility(View.VISIBLE);

        if (playButton.get() != null) {
            playButton.get().setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        String urlImage = params[0];
        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL(urlImage);

            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200)
                return null;

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null)
                return BitmapFactory.decodeStream(inputStream);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        progressBar.setVisibility(View.INVISIBLE);
        if (playButton.get() != null) {
            playButton.get().setVisibility(View.VISIBLE);
        }


        if (isCancelled())
            bitmap = null;

        ImageView imageView = this.imageViewWeakReference.get();
        if (imageView != null && bitmap != null) {

            if(shadowEnabled){
                LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(imageView.getContext(),
                        R.drawable.shadows);
                if (drawable != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    drawable.setDrawableByLayerId(R.id.shadows_cover_drawble, bitmapDrawable);
                    imageView.setImageDrawable(drawable);
                }

            }else {
                if (bitmap.getWidth() < imageView.getWidth() ||
                        bitmap.getHeight() < imageView.getHeight()) {
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) imageView.getWidth() / (float) bitmap.getWidth(),
                            (float) imageView.getHeight() / (float) bitmap.getHeight());
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                            matrix, false);
                }

                imageView.setImageBitmap(bitmap);
            }

        }
    }
}
