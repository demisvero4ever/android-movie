package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieDetailsContract {

    interface MovieDetailsView{
        void showMovieDetails(MovieModel movie);
    }

    interface MovieDetailsPresenter{
        void loadMovieDetails(int movieId);
        void dismissMovieDetails();
    }
}
