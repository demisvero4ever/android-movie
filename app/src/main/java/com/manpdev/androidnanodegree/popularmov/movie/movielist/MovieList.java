package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;
import com.manpdev.androidnanodegree.popularmov.movie.tasks.SyncMovieTask;
import com.manpdev.androidnanodegree.popularmov.services.SyncDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieList implements MovieListContract.PopularMovieListPresenter,
        LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MovieList";

    private static String[] DATA_PROJECTION = new String[]{
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_POSTER
    };

    private Context mContext;

    private static final int MOVIE_LOADER_ID = 214;
    private LoaderManager mLoadManager;

    private String mSortingOption;
    private boolean mSyncFailed;

    private MovieListContract.PopularMovieListView mView;
    private String mPosterApiPath;

    private BroadcastReceiver mDataSyncReceiver;


    public MovieList(Context context, MovieListContract.PopularMovieListView view, LoaderManager loaderManager) {
        this.mContext = context;
        this.mView = view;
        this.mLoadManager = loaderManager;
        this.mPosterApiPath = mContext.getString(R.string.movie_db_poster_api_url);
        this.mSortingOption = getSortingString();
    }

    @Override
    public void loadMovieList() {
        mLoadManager.initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void dismissMovieList() {
        mLoadManager.destroyLoader(MOVIE_LOADER_ID);
    }

    @Override
    public void registerListeners() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SyncDataService.ACTION_SYNC_COMPLETED);
        filter.addAction(SyncDataService.ACTION_SYNC_FAILED);

        this.mDataSyncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleSyncResult(intent);
            }
        };

        mContext.registerReceiver(mDataSyncReceiver, filter);
        Preferences.registerPreferencesListener(mContext, this);
    }

    @Override
    public void unregisterListener() {
        mContext.unregisterReceiver(mDataSyncReceiver);
        Preferences.registerPreferencesListener(mContext, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY),
                DATA_PROJECTION,
                null,
                null,
                mSortingOption
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, String.format("onLoadFinished : %d items", data.getCount()));
        
        if(data.getCount() == 0 && !mSyncFailed)
            startSyncData();

        mView.showMovieList(buildMovieModelList(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(Preferences.SELECTED_SORTING_OPTION))
            startSyncData();
    }

    private void refreshMovieList() {
        mLoadManager.restartLoader(MOVIE_LOADER_ID, null, this);
    }

    private void startSyncData() {
        SyncDataService.startSyncData(mContext, SyncMovieTask.TASK_ID, true);
    }

    private List<MovieModel> buildMovieModelList(Cursor data) {
        List<MovieModel> result = new ArrayList<>();
        if(data == null || !data.moveToFirst())
            return result;

        MovieModel model;
        String posterPath;

        while(!data.isAfterLast()){
            model = new MovieModel();
            model.setId(data.getInt(0));
            posterPath = data.getString(1);

            if(!TextUtils.isEmpty(posterPath))
                model.setPosterPath(mPosterApiPath + posterPath);
            result.add(model);
            data.moveToNext();
        }

        return result;
    }

    private void handleSyncResult(Intent intent) {
        Log.d(TAG, "handleSyncResult: " + intent.getAction());

        if(intent.getIntExtra(SyncDataService.EXTRA_TASK_ID, 0) != SyncMovieTask.TASK_ID)
            return;

        if(intent.getAction().equals(SyncDataService.ACTION_SYNC_FAILED)) {
            this.mSyncFailed = true;
            mView.showMessage(R.string.sync_data_failed);
        }else
            this.mSyncFailed = false;

        if(!mSortingOption.equals(getSortingString())) {
            this.mSortingOption = getSortingString();
        }

        refreshMovieList();
    }

    public String getSortingString() {
        switch (Preferences.getSortingOption(mContext)){
            case Preferences.SORT_POPULARITY_DESC:
                return MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
            case Preferences.SORT_VOTE_AVERAGE_DESC:
                return MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " DESC";
        }
        return "";
    }


}
