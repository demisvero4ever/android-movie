package com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

/**
 * novoa on 3/22/16.
 */
public interface MovieDetailsListener {
    void setMovieAsFavorite();
    void removeMovieFromFavorites();

    void openTrailerVideo(String videoId);
    void openWebReview(String url);
}
