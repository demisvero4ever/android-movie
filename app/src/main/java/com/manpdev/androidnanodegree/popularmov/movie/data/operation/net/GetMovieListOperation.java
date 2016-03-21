package com.manpdev.androidnanodegree.popularmov.movie.data.operation.net;

import android.content.Context;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.tasks.Task;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieWrapperModel;

import java.io.IOException;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class GetMovieListOperation extends Task<MovieWrapperModel> {

    private static final String TAG = "GetMovieListOperation";
    
    private String mSelectionOption;
    private MovieApiRequester mRequester;

    public GetMovieListOperation(Context mContext) {
        this.mRequester = MovieApiRequester.getInstance(mContext);

        this.mSelectionOption = Preferences.BY_POPULARITY_DESC;
    }

    public void setSelectionOption(@Preferences.SelectionOptions String selectionOption) {
        this.mSelectionOption = selectionOption;
    }

    @Override
    public MovieWrapperModel execute() throws Throwable {
        try {
            Log.d(TAG, "execute() called");
            
            Call<MovieWrapperModel> request;
            switch (mSelectionOption) {
                case Preferences.BY_VOTE_AVERAGE_DESC:
                    request = mRequester.getMovieApi()
                            .getTopRatedMovies(mRequester.getMovieApiKey());
                    break;

                case Preferences.BY_POPULARITY_DESC:
                default:
                    request = mRequester.getMovieApi()
                            .getPopularMovies(mRequester.getMovieApiKey());
                    break;
            }

            Response<MovieWrapperModel> response = request.execute();

            if (!response.isSuccess())
                throw new Throwable(response.errorBody().string());

            return response.body();

        } catch (IOException e) {
            throw new Throwable(e.getMessage());
        }
    }
}
