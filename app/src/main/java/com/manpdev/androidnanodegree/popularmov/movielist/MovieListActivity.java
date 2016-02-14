package com.manpdev.androidnanodegree.popularmov.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.moviedetails.MovieDetailsActivity;
import com.manpdev.androidnanodegree.popularmov.moviedetails.MovieDetailsFragment;

public class MovieListActivity extends AppCompatActivity implements MovieListFragment.MovieSelectionListener{

    private static final String MOVIE_DETAIL_TAG = "DETAIL_FRAGMENT";
    private boolean mTwoPanels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.fragment_movie_details_container) != null) {
            mTwoPanels = true;
            updateMovieDetailFragment(0);
        }
    }

    @Override
    public void onSelectMovie(int id) {
        if(mTwoPanels){
            updateMovieDetailFragment(id);
        }else{
            startMovieDetailActivity(id);
        }
    }

    private void startMovieDetailActivity(int id) {
        Bundle arg = new Bundle();
        arg.putInt(MovieDetailsFragment.MOVIE_ID_EXTRA, id);

        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        intent.putExtras(arg);

        startActivity(intent);
    }

    private void updateMovieDetailFragment(int id) {
        Bundle arg = new Bundle();
        arg.putInt(MovieDetailsFragment.MOVIE_ID_EXTRA, id);

        MovieDetailsFragment fragment = new MovieDetailsFragment();
        fragment.setArguments(arg);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_details_container, fragment, MOVIE_DETAIL_TAG)
                .commit();
    }
}
