package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.MovieDetailsActivity;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.MovieDetailsFragment;

public class MovieListActivity extends AppCompatActivity implements MovieSelectionListener {

    private static final String TAG = "MovieListActivity";
    private static final String MOVIE_DETAIL_TAG = "DETAIL_FRAGMENT";
    public static final String STATE_MOVIE = "::state_movie";

    private boolean mTwoPanels;
    private MovieModel mSelectedMovie;

    private ViewGroup mDetailFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (savedInstanceState != null) {
            mSelectedMovie = savedInstanceState.getParcelable(STATE_MOVIE);
        }

        mDetailFragmentContainer = (ViewGroup) findViewById(R.id.fragment_movie_details_container);

        if (mDetailFragmentContainer != null) {
            mTwoPanels = true;
            MovieDetailsFragment mDetailsFragment = new MovieDetailsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_movie_details_container, mDetailsFragment, MOVIE_DETAIL_TAG)
                    .commit();
        }else{
            Fragment detailsFragment = getSupportFragmentManager().findFragmentByTag(MOVIE_DETAIL_TAG);

            if (detailsFragment != null)
                getSupportFragmentManager().beginTransaction()
                        .remove(detailsFragment)
                        .commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            initializeWindowsTransition();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MOVIE, mSelectedMovie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_list_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        switch (Preferences.getSelectionOption(this)) {
            case Preferences.BY_POPULARITY_DESC:
                menu.findItem(R.id.op_select_by_popularity).setChecked(true);
                break;
            case Preferences.BY_VOTE_AVERAGE_DESC:
                menu.findItem(R.id.op_select_by_vote_avg).setChecked(true);
                break;
            case Preferences.FAVORITES:
                menu.findItem(R.id.op_select_favorites).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.op_refresh_list:
                refreshData();
                return true;
            case R.id.op_select_favorites:
                Preferences.setSelectionOption(this, Preferences.FAVORITES);
                item.setChecked(true);
                return true;
            case R.id.op_select_by_popularity:
                Preferences.setSelectionOption(this, Preferences.BY_POPULARITY_DESC);
                item.setChecked(true);
                return true;
            case R.id.op_select_by_vote_avg:
                Preferences.setSelectionOption(this, Preferences.BY_VOTE_AVERAGE_DESC);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mTwoPanels && mSelectedMovie != null)
            updateMovieDetailFragment();
    }

    private void refreshData() {
        FragmentManager fm = getSupportFragmentManager();

        MovieListFragment fragment = (MovieListFragment) fm.findFragmentById(R.id.fragment_movie_list);

        if (fragment != null && !fragment.isDetached())
            fragment.doRefresh();
    }

    @Override
    public void onSelectMovie(View holder, MovieModel movie) {
        mSelectedMovie = movie;

        if (mTwoPanels) {
            updateMovieDetailFragment();
        } else {
            startMovieDetailActivity(holder);
        }
    }

    @Override
    public void refreshDetails(MovieModel movie) {
        Log.d(TAG, "refreshDetails() called with: " + "movie = [" + movie + "]");
        mSelectedMovie = movie;
        if (mTwoPanels)
            updateMovieDetailFragment();
    }

    @Override
    public void clearSelection() {
        mSelectedMovie = null;

        if(mTwoPanels)
            mDetailFragmentContainer.setVisibility(View.INVISIBLE);
    }

    private void updateMovieDetailFragment() {
        mDetailFragmentContainer.setVisibility(View.VISIBLE);

        MovieDetailsFragment detailsFragment = (MovieDetailsFragment) getSupportFragmentManager()
                .findFragmentByTag(MOVIE_DETAIL_TAG);

        if (detailsFragment != null && !detailsFragment.isDetached()) {
            detailsFragment.updateMovie(mSelectedMovie);
        }
    }

    private void startMovieDetailActivity(View holder) {
        Bundle arg = new Bundle();
        arg.putParcelable(MovieSelectionListener.EXTRA_MOVIE, mSelectedMovie);

        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        intent.putExtras(arg);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, holder, getString(R.string.movie_poster_resource));

            startActivity(intent, options.toBundle());
        } else
            startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initializeWindowsTransition() {
        getWindow().setExitTransition(new Fade(Visibility.MODE_OUT));
        getWindow().setReenterTransition(new Explode());
    }
}
