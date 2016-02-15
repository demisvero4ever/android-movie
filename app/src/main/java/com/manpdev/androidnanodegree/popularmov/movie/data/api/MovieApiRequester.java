package com.manpdev.androidnanodegree.popularmov.movie.data.api;

import android.content.Context;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by novoa.pro@gmail.com on 2/15/16
 */
public class MovieApiRequester {

    public static final String HTTP_API_THE_MOVIE_DB_ORG = "http://api.themoviedb.org";
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
                .baseUrl(HTTP_API_THE_MOVIE_DB_ORG)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.mMovieApi = retrofit.create(MoviesApi.class);
        this.mApiKey = context.getResources().getString(R.string.movie_api_key);
    }

    public void getMovieList(int page, Callback<MovieWrapperModel> callback){
        this.mMovieApi.getPopularMovieList(this.mApiKey, page).enqueue(callback);
    }

}
