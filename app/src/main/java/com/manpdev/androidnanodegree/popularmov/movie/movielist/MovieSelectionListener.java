package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.view.View;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

/**
 * Created by novoa.pro@gmail.com on 2/24/16
 */
public interface MovieSelectionListener {
    String EXTRA_MOVIE = "::extra_movie";
    void onSelectMovie(MovieModel movie);
    void refreshDetails(MovieModel movie);
    void clearSelection();
 }
