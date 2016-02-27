package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.MovieDetailsActivity;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.MovieDetailsFragment;
import com.manpdev.androidnanodegree.popularmov.movie.tasks.SyncMovieTask;
import com.manpdev.androidnanodegree.popularmov.services.SyncDataService;

public class MovieListActivity extends AppCompatActivity implements MovieSelectionListener{

    private static final String MOVIE_DETAIL_TAG = "DETAIL_FRAGMENT";
    public static final String STATE_MOVIE_ID = "::state_movie_id";

    private boolean mTwoPanels;
    private int mMovieId;

    private ViewGroup mDetailFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if(savedInstanceState != null) {
            mMovieId = savedInstanceState.getInt(STATE_MOVIE_ID);
        }

        mDetailFragmentContainer = (ViewGroup) findViewById(R.id.fragment_movie_details_container);

        if (mDetailFragmentContainer != null) {
            mTwoPanels = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_MOVIE_ID, mMovieId);
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

        switch (Preferences.getSortingOption(this)){
            case Preferences.SORT_POPULARITY_DESC:
                menu.findItem(R.id.op_sort_by_popularity).setChecked(true);
                break;
            case Preferences.SORT_VOTE_AVERAGE_DESC:
                menu.findItem(R.id.op_sort_by_vote_avg).setChecked(true);
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
            case R.id.op_sort_by_popularity:
                Preferences.setSortingOption(this, Preferences.SORT_POPULARITY_DESC);
                item.setChecked(true);
                return true;
            case R.id.op_sort_by_vote_avg:
                Preferences.setSortingOption(this, Preferences.SORT_VOTE_AVERAGE_DESC);
                item.setChecked(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mTwoPanels)
            updateMovieDetailFragment(mMovieId);
    }

    private void refreshData() {
        SyncDataService.startSyncData(this, SyncMovieTask.TASK_ID, true);
    }

    @Override
    public void onSelectMovie(int id) {
        mMovieId = id;

        if(mTwoPanels){
            updateMovieDetailFragment(mMovieId);
        }else{
            startMovieDetailActivity(mMovieId);
        }
    }

    private void startMovieDetailActivity(int id) {
        Bundle arg = new Bundle();
        arg.putInt(MovieSelectionListener.EXTRA_MOVIE_ID, id);

        Intent intent = new Intent(MovieListActivity.this, MovieDetailsActivity.class);
        intent.putExtras(arg);

        startActivity(intent);
    }

    private void updateMovieDetailFragment(int id) {
        mDetailFragmentContainer.setVisibility(View.VISIBLE);

        Bundle arg = new Bundle();
        arg.putInt(MovieSelectionListener.EXTRA_MOVIE_ID, id);

        MovieDetailsFragment mDetailsFragment = new MovieDetailsFragment();
        mDetailsFragment.setArguments(arg);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_movie_details_container, mDetailsFragment, MOVIE_DETAIL_TAG)
                .commit();
    }
}
