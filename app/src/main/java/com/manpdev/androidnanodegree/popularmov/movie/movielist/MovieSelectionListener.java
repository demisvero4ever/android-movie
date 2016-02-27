package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.view.View;

/**
 * Created by novoa.pro@gmail.com on 2/24/16
 */
public interface MovieSelectionListener {
    String EXTRA_MOVIE_ID = "::extra_movie_id";
    void onSelectMovie(View holder, int id);
 }
