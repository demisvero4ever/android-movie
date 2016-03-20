package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieDetailsContract {

    interface MovieDetailsView{
        void showMovieTrailers(List<Object> movie);
        void showMovieReviews(List<Object> movie);
        void favoriteSelection(boolean enable);
    }

    interface MovieDetailsPresenter{
        void loadMovieDetails(int cloudMovieId);
        void saveMovieAsFavorite(MovieModel movie);
        void removeMovieFromFavorites(int id);
    }
}
