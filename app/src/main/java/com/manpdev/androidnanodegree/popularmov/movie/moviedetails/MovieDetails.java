package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
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

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieDetails implements MovieDetailsContract.MovieDetailsPresenter,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MovieDetails";

    private static String[] DATA_PROJECTION = new String[]{
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_SYNOPSIS,
    };

    private final String mPosterApiPath;
    private int mMovieId;

    private Context mContext;

    private static final int MOVIE_LOADER_ID = 215;
    private LoaderManager mLoadManager;

    private MovieDetailsContract.MovieDetailsView mView;

    public MovieDetails(Context mContext, MovieDetailsContract.MovieDetailsView view, LoaderManager mLoadManager) {
        this.mContext = mContext;
        this.mLoadManager = mLoadManager;
        this.mPosterApiPath = mContext.getString(R.string.movie_db_poster_api_url);
        this.mView = view;
    }

    @Override
    public void loadMovieDetails(int movieId) {
        this.mMovieId = movieId;

        mLoadManager.initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public void dismissMovieDetails() {
        mLoadManager.destroyLoader(MOVIE_LOADER_ID);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mMovieId == 0)
            return new CursorLoader(mContext,
                    MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY),
                    DATA_PROJECTION,
                    null,
                    null,
                    getSortingString()
            );
        else
            return new CursorLoader(mContext,
                    MovieContract.MovieEntry.buildUriForMovieById(MoviesProvider.sAUTHORITY,
                            mMovieId),
                    DATA_PROJECTION,
                    null,
                    null,
                    getSortingString()
            );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            mView.showMovieDetails(buildMovie(data));

        }else if(mMovieId != 0) {
            mMovieId = 0;
            mLoadManager.restartLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private MovieModel buildMovie(Cursor data) {
        if (!data.moveToFirst())
            return null;

        MovieModel model = new MovieModel();

        model.setId(data.getInt(0));
        model.setTitle(data.getString(1));
        if (!TextUtils.isEmpty(data.getString(2)))
            model.setPosterPath(mPosterApiPath + data.getString(2));

        try {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(data.getLong(3));
            model.setReleaseDate(String.valueOf(c.get(Calendar.YEAR)));
        } catch (Exception ex) {
            Log.e(TAG, "buildMovie: ", ex);
            model.setReleaseDate("");
        }

        model.setVoteAverage(data.getDouble(4));
        model.setOverview(data.getString(5));

        return model;
    }

    public String getSortingString() {
        switch (Preferences.getSortingOption(mContext)){
            case Preferences.SORT_POPULARITY_DESC:
                return String.format(Locale.US, "%s DESC", MovieContract.MovieEntry.COLUMN_POPULARITY);
            case Preferences.SORT_VOTE_AVERAGE_DESC:
                return String.format(Locale.US, "%s DESC", MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        }
        return "";
    }
}
