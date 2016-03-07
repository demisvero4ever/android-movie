package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieListContract {

    interface PopularMovieListView{
        void showMovieList(List<MovieModel> list);
        void showMessage(int resourceId);
    }

    interface PopularMovieListPresenter{
        void register();
        void unregister();

        void loadMovieList();
        void dismissMovieList();
    }
}
