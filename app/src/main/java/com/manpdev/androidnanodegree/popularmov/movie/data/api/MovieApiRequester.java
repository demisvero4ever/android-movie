package com.manpdev.androidnanodegree.popularmov.movie.data.api;

import android.content.Context;

import com.manpdev.androidnanodegree.popularmov.BuildConfig;
import com.manpdev.androidnanodegree.popularmov.R;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by novoa.pro@gmail.com on 2/15/16
 */
public class MovieApiRequester {

    private static MovieApiRequester sInstance;

    private MovieApi mMovieApi;
    private String mApiKey;

    public static MovieApiRequester getInstance(Context context) {
        if(sInstance == null)
            sInstance = new MovieApiRequester(context);

        return sInstance;
    }

    public MovieApiRequester(Context context) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.movie_db_api_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.mMovieApi = retrofit.create(MovieApi.class);

        this.mApiKey = BuildConfig.MOVIE_API_KEY;
    }

    public String getMovieApiKey(){
        return this.mApiKey;
    }

    public MovieApi getMovieApi() {
        return mMovieApi;
    }
}
