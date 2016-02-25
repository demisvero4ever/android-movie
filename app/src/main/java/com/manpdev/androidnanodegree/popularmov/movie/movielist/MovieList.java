package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.manpdev.androidnanodegree.popularmov.R;
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
        LoaderManager.LoaderCallbacks<Cursor> {

    private static String[] dataProjection = new String[]{
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_POSTER
    };

    private MovieListContract.PopularMovieListView mView;
    private String mPosterApiPath;

    private BroadcastReceiver mDataSyncReceiver;

    private static final int MOVIE_LOADER_ID = 214;
    private LoaderManager mLoadManager;

    public MovieList(MovieListContract.PopularMovieListView view, LoaderManager loaderManager) {
        this.mView = view;
        this.mLoadManager = loaderManager;
        this.mPosterApiPath = view.getContext().getString(R.string.movie_db_poster_api_url);
    }

    @Override
    public void loadMovieList() {
        mLoadManager.initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void refreshMovieList() {
        mLoadManager.restartLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void startSyncData() {
        SyncDataService.startSyncData(mView.getContext(), SyncMovieTask.TASK_ID, true);
    }

    @Override
    public void registerSyncDataListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SyncDataService.ACTION_SYNC_COMPLETED);
        filter.addAction(SyncDataService.ACTION_SYNC_FAILED);

        this.mDataSyncReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                handleSyncResult(intent);
            }
        };

        mView.getContext().registerReceiver(mDataSyncReceiver, filter);
    }

    @Override
    public void unregisterSyncDataListener() {
        mView.getContext().unregisterReceiver(mDataSyncReceiver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mView.getContext(),
                MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY),
                dataProjection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mView.showMovieList(buildMovieModelList(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
        if(intent.getIntExtra(SyncDataService.EXTRA_JOD_ID, 0) != SyncMovieTask.TASK_ID)
            return;

        if(intent.getAction().equals(SyncDataService.ACTION_SYNC_FAILED)) {
            mView.showMessage(R.string.sync_data_failed);
            return;
        }

        refreshMovieList();
    }

}
