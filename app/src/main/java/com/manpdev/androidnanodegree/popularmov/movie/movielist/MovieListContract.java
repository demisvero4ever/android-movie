package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieListContract {

    interface PopularMovieListView{
        Context getContext();
        void showPosterList(List<MovieModel> list);
    }

    interface PopularMovieListPresenter{
        void loadMovieList(LoaderManager loaderManager);
        void refreshMovieList(LoaderManager loaderManager);
    }
}
