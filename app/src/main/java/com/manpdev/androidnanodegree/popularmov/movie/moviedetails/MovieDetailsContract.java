package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieDetailsContract {

    interface MovieDetailsView{
        void showMovieTrailers(List<MovieTrailerModel> trailers);
        void showMovieReviews(List<MovieReviewModel> reviews);
        void favoriteSelection(boolean enable);
    }

    interface MovieDetailsPresenter{
        void loadMovieDetails(int cloudMovieId);
        void saveMovieAsFavorite(MovieModel movie);
        void removeMovieFromFavorites(int id);
    }
}
