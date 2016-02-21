package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

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

    public MovieList( MovieListContract.PopularMovieListView view) {
        this.mView = view;
        this.mPosterApiPath = view.getContext().getString(R.string.movie_db_poster_api_url);
    }

    @Override
    public void loadMovieList(LoaderManager loaderManager) {
        int MOVIE_LOADER_ID = 214;
        loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
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
        mView.showPosterList(buildMovieModelList(data));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private List<MovieModel> buildMovieModelList(Cursor data) {
        List<MovieModel> result = new ArrayList<>();
        if(data == null || !data.moveToFirst())
            return result;

        MovieModel model;

        while(!data.isAfterLast()){
            model = new MovieModel();
            model.setId(data.getInt(0));
            model.setPosterPath(mPosterApiPath + data.getString(1));
            result.add(model);
            data.moveToNext();
        }

        return result;
    }

}
