package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.common.tasks.Callback;
import com.manpdev.androidnanodegree.popularmov.common.tasks.TaskProcessor;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.net.GetMovieListOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieList implements MovieListContract.PopularMovieListPresenter,
        LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "MovieList";

    private Context mContext;

    private static final int MOVIE_LOADER_ID = 214;
    private LoaderManager mLoadManager;

    private MovieListContract.PopularMovieListView mView;

    private static final String GET_MOVIE_LIST_OP_ID = "movie_list_operation";
    private TaskProcessor mTaskProcessor;
    private final GetMovieListOperation mGetMovieListOperation;

    private Callback<MovieWrapperModel> mMovieListObserver = new Callback<MovieWrapperModel>() {
        @Override
        public void onResult(MovieWrapperModel result) {
            Log.d(TAG, "onResult: " + result.getResults().size());
            mView.showMovieList(result.getResults());
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onError: ", th);
            mView.showMessage(R.string.sync_data_failed);
        }
    };

    public MovieList(Context context, MovieListContract.PopularMovieListView view, LoaderManager loaderManager) {
        this.mContext = context;
        this.mView = view;
        this.mLoadManager = loaderManager;
        this.mTaskProcessor = new TaskProcessor();
        this.mGetMovieListOperation  = new GetMovieListOperation(mContext);
    }

    @Override
    public void loadMovieList() {
        refreshMovieList();
    }

    @Override
    public void dismissMovieList() {
        mLoadManager.destroyLoader(MOVIE_LOADER_ID);
    }

    @Override
    public void register() {
        Preferences.registerPreferencesListener(mContext, this);
    }

    @Override
    public void unregister() {
        Preferences.unregisterPreferencesListener(mContext, this);
        this.mTaskProcessor.unSubscribeOperation(GET_MOVIE_LIST_OP_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext,
                MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY),
                MovieContract.MovieEntry.COLUMNS_ALL,
                null,
                null,
                String.format("%s DESC", MovieContract.MovieEntry._ID)
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, String.format("onLoadFinished : %d items", data.getCount()));
        mView.showMovieList(buildMovieModelList(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Preferences.MOVIE_SELECTION_OPTION))
            refreshMovieList();
    }

    private void refreshMovieList() {
        if (Preferences.getSelectionOption(mContext).equals(Preferences.FAVORITES))
            mLoadManager.restartLoader(MOVIE_LOADER_ID, null, this);
        else {
            this.mGetMovieListOperation.setSelectionOption(Preferences.getSelectionOption(mContext));

            this.mTaskProcessor.perform(GET_MOVIE_LIST_OP_ID,
                    mGetMovieListOperation,
                    mMovieListObserver);
        }
    }

    private List<MovieModel> buildMovieModelList(Cursor data) {
        List<MovieModel> result = new ArrayList<>();
        if (data == null || !data.moveToFirst())
            return result;

        MovieModel model;

        while (!data.isAfterLast()) {
            model = new MovieModel();
            model.setId(data.getInt(1));
            model.setTitle(data.getString(2));
            model.setPosterPath(data.getString(3));
            model.setOverview(data.getString(4));
            model.setVoteAverage(data.getDouble(5));
            model.setReleaseDate(new SimpleDateFormat(MovieModel.RElEASE_DATE_FORMAT, Locale.US)
                    .format(new Date(data.getLong(6))));
            model.setPopularity(data.getDouble(7));

            result.add(model);
            data.moveToNext();
        }
        return result;
    }
}
