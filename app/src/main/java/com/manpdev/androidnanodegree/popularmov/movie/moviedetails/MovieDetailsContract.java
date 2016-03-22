package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieExtrasModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieDetailsContract {

    interface MovieDetailsView{
        void showMovieExtras(MovieExtrasModel extras);
        void favoriteSelection(boolean enable);
    }

    interface MovieDetailsPresenter{
        void loadMovieDetails(int cloudMovieId);
        void saveMovieAsFavorite(MovieModel movie);
        void removeMovieFromFavorites(int id);
    }
}
