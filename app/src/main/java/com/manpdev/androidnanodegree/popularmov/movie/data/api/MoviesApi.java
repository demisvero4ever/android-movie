package com.manpdev.androidnanodegree.popularmov.movie.data.api;

import android.support.annotation.StringDef;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MoviesApi {

    @GET("3/discover/movie")
    Call<MovieWrapperModel> getSortedMovieList(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);
}
