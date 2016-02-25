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
        void showMovieList(List<MovieModel> list);
        void showMessage(int resourceId);
    }

    interface PopularMovieListPresenter{
        void registerSyncDataListener();
        void unregisterSyncDataListener();
        void startSyncData();

        void loadMovieList();
        void refreshMovieList();
    }
}
