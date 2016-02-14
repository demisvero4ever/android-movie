package com.manpdev.androidnanodegree.popularmov.moviedetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manpdev.androidnanodegree.popularmov.R;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String MOVIE_DETAIL_TAG = "DETAIL_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        if(savedInstanceState == null){
            updateMovieDetailFragment(getIntent().getExtras());
        }
    }

    private void updateMovieDetailFragment(Bundle arg) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(arg);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_details_container, fragment, MOVIE_DETAIL_TAG)
                .commit();
    }
}
