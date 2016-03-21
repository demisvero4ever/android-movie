package com.manpdev.androidnanodegree.popularmov.movie.data.operation.net;

import android.content.Context;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.tasks.Task;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieReviewWrapperModel;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class GetMovieReviewsOperation extends Task<MovieReviewWrapperModel> {

    private static final String TAG = "GetMovReviewsOperation";

    private int mMovieId;
    private MovieApiRequester mRequester;

    public GetMovieReviewsOperation(Context mContext, int movieId) {
        this.mRequester = MovieApiRequester.getInstance(mContext);
        this.mMovieId = movieId;
    }

    public void setMovieID(int movieId) {
        this.mMovieId = movieId;
    }

    @Override
    public MovieReviewWrapperModel execute() throws Throwable {
        try {
            Log.d(TAG, "execute() called");

            Call<MovieReviewWrapperModel> request = mRequester.getMovieApi()
                    .getMovieReviews(mMovieId, mRequester.getMovieApiKey());

            Response<MovieReviewWrapperModel> response = request.execute();

            if (!response.isSuccess())
                throw new Throwable(response.errorBody().string());

            return response.body();

        } catch (IOException e) {
            throw new Throwable(e.getMessage());
        }
    }
}
