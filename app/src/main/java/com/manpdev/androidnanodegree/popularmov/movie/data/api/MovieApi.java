package com.manpdev.androidnanodegree.popularmov.movie.data.api;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieReviewWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieTrailerWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieWrapperModel;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public interface MovieApi {

    @GET("3/movie/popular")
    Call<MovieWrapperModel> getPopularMovies(@Query("api_key") String apiKey);

    @GET("3/movie/top_rated")
    Call<MovieWrapperModel> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("3/movie/{movieId}/reviews")
    Call<MovieReviewWrapperModel> getMovieReviews(@Path("movieId") int movieId, @Query("api_key") String apiKey);

    @GET("3/movie/{movieId}/videos")
    Call<MovieTrailerWrapperModel> getMovieTrailers(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
