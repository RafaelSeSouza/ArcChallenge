package com.arctouch.movies.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.movies.R;
import com.arctouch.movies.data.UpcomingMovieData;
import com.arctouch.movies.viewmodel.MovieViewModel;
import com.arctouch.movies.viewmodel.MovieViewModelFactory;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";

    private MovieViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mViewModel = ViewModelProviders.of(this, new MovieViewModelFactory(getApplication())).get(MovieViewModel.class);

        // Add the actionbar and set the up button to return to the main activity
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        refreshUi();
    }

    public void refreshUi() {
        final int id = getIntent().getIntExtra(EXTRA_MOVIE_ID, -1);

        final TextView textTitle = findViewById(R.id.detail_text_title);
        final ImageView imagePoster = findViewById(R.id.detail_image_poster);
        final TextView textGenre = findViewById(R.id.detail_text_genre);
        final TextView textOverview = findViewById(R.id.detail_text_overview);
        final TextView textRelease = findViewById(R.id.detail_text_release_date);

        mViewModel.getUpcomingMovie(id).observe(this, new Observer<UpcomingMovieData>() {
            @Override
            public void onChanged(@Nullable UpcomingMovieData upcomingMovieData) {
                if (upcomingMovieData == null) {
                    Log.e(LOG_TAG, "Movie ID " + id + " not found in cache.");
                    return;
                }
                Log.d(LOG_TAG, "Refrshing Details for Movie ID " + id + " - " + upcomingMovieData.getTitle());

                textTitle.setText(upcomingMovieData.getTitle());
                mViewModel.getPosterImage(upcomingMovieData, imagePoster);
                textGenre.setText(mViewModel.getGenresCommaSeparated(upcomingMovieData));
                textOverview.setText(upcomingMovieData.getOverview());
                textRelease.setText(upcomingMovieData.getReleaseDate());
            }
        });

    }
}
