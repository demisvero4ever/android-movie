package com.manpdev.androidnanodegree.popularmov.movie.data.api;

import android.content.Context;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;

import java.util.Locale;

import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by novoa.pro@gmail.com on 2/15/16
 */
public class MovieApiRequester {

    private static MovieApiRequester mInstance;

    private MoviesApi mMovieApi;
    private String mApiKey;

    public static MovieApiRequester getInstance(Context context) {
        if(mInstance == null)
            mInstance = new MovieApiRequester(context);

        return mInstance;
    }

    public MovieApiRequester(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.movie_db_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.mMovieApi = retrofit.create(MoviesApi.class);
        this.mApiKey = context.getResources().getString(R.string.movie_api_key);
    }

    public MovieWrapperModel getMovieList(int page) throws Throwable {
        try {
            Response<MovieWrapperModel> response = this.mMovieApi.getPopularMovieList(this.mApiKey, page).execute();

            if(response.code() != 200)
                throw new Throwable(String.format(Locale.US, "Http Status: %d", response.code()));

            return response.body();

        }catch (Exception ex){
            throw new Throwable(ex.getMessage());
        }
    }

}
