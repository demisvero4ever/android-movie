package com.manpdev.androidnanodegree.popularmov.movie.data.operation.net;

import android.content.Context;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.tasks.Task;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieExtrasModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieReviewWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieTrailerWrapperModel;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class GetMovieExtrasOperation extends Task<MovieExtrasModel> {

    private static final String TAG = "GetMovExtrasOperation";

    private int mMovieId;
    private MovieApiRequester mRequester;

    public GetMovieExtrasOperation(Context mContext, int movieId) {
        this.mRequester = MovieApiRequester.getInstance(mContext);
        this.mMovieId = movieId;
    }

    @Override
    public MovieExtrasModel execute() throws Throwable {
        MovieExtrasModel extras = new MovieExtrasModel(mMovieId);
        extras.setTrailers(getMovieTrailers().getResults());
        extras.setReviews(getMovieReviews().getResults());

        return extras;
    }

    private MovieReviewWrapperModel getMovieReviews(){
        try {
            Log.d(TAG, "execute() called");

            Call<MovieReviewWrapperModel> request = mRequester.getMovieApi()
                    .getMovieReviews(mMovieId, mRequester.getMovieApiKey());

            Response<MovieReviewWrapperModel> response = request.execute();
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, "execute: ", e);
            return new MovieReviewWrapperModel();
        }
    }

    private MovieTrailerWrapperModel getMovieTrailers(){
        try {
            Log.d(TAG, "execute() called");

            Call<MovieTrailerWrapperModel> request = mRequester.getMovieApi()
                    .getMovieTrailers(mMovieId, mRequester.getMovieApiKey());

            Response<MovieTrailerWrapperModel> response = request.execute();
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, "execute: ", e);
            return new MovieTrailerWrapperModel();
        }
    }

}
