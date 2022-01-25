package br.com.netflix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
    }
}