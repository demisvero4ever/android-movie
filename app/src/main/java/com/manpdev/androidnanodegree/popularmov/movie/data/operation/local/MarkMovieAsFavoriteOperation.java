package com.manpdev.androidnanodegree.popularmov.movie.data.operation.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.taskproc.Task;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class MarkMovieAsFavoriteOperation extends Task<Boolean> {

    private static final String TAG = "MarkMovAsFavOperation";

    private MovieModel mMovie;
    private ContentResolver mContentResolver;

    public MarkMovieAsFavoriteOperation(Context mContext, MovieModel movie) {
        this.mContentResolver = mContext.getContentResolver();
        this.mMovie = movie;
    }

    @Override
    public Boolean execute() throws Throwable {
        try {
            Log.d(TAG, "execute() called");

            if(mMovie == null)
                return false;

            ContentValues cv = new ContentValues();
            buildMovieContent(cv);

            Uri inserted = mContentResolver.insert(MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY),
                    cv);

            return inserted != null;

        } catch (Exception e) {
            throw new Throwable(e.getMessage());
        }
    }

    private void buildMovieContent(ContentValues cv){
        cv.put(MovieContract.MovieEntry.COLUMN_CLOUD_ID, mMovie.getId());
        cv.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        cv.put(MovieContract.MovieEntry.COLUMN_POPULARITY, mMovie.getPopularity());
        cv.put(MovieContract.MovieEntry.COLUMN_POSTER, mMovie.getPoster());
        cv.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        cv.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, mMovie.getOverview());
        cv.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
    }


}
