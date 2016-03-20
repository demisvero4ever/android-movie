package com.manpdev.androidnanodegree.popularmov.movie.data.operation.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.taskproc.Task;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class FavoriteMovieCheckOperation extends Task<Boolean> {

    private static final String TAG = "FavMovCheckOperation";

    private int mMovieId;
    private ContentResolver mContentResolver;

    public FavoriteMovieCheckOperation(Context mContext, int movieId) {
        this.mContentResolver = mContext.getContentResolver();
        this.mMovieId = movieId;
    }

    @Override
    public Boolean execute() throws Throwable {
        try {
            Log.d(TAG, "execute() called");

            Cursor movieCursor =  mContentResolver.query(MovieContract.MovieEntry.buildUriForMovieById(MoviesProvider.sAUTHORITY,
                    mMovieId), new String[]{MovieContract.MovieEntry.COLUMN_CLOUD_ID}, null, null, null);

            int counter = movieCursor.getCount();

            movieCursor.close();

            return counter > 0;
        } catch (Exception e) {
            throw new Throwable(e.getMessage());
        }
    }
}
