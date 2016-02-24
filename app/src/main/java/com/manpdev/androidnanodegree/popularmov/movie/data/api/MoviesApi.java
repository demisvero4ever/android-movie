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

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SORT_POPULARITY_DESC,
            SORT_VOTE_AVERAGE_DESC
    })
    @interface SortingOptions {}
    String SORT_POPULARITY_DESC = "popularity.desc";
    String SORT_VOTE_AVERAGE_DESC = "vote_average.desc";

    @GET("3/discover/movie")
    Call<MovieWrapperModel> getSortedMovieList(@Query("api_key") String apiKey, @Query("sort_by") String sortBy);
}
