package com.manpdev.androidnanodegree.popularmov.movie.data.operation.local;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.taskproc.Task;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class RemoveMovieFromFavoritesOperation extends Task<Boolean> {

    private static final String TAG = "RemMovieFromFavoritesOp";

    private int mMovieId;
    private ContentResolver mContentResolver;

    public RemoveMovieFromFavoritesOperation(Context mContext, int movieId) {
        this.mContentResolver = mContext.getContentResolver();
        this.mMovieId = movieId;
    }

    @Override
    public Boolean execute() throws Throwable {
        try {
            Log.d(TAG, "execute() called");
            int deleted = mContentResolver.delete(MovieContract.MovieEntry.buildUriForMovieById(MoviesProvider.sAUTHORITY, mMovieId), null, null);
            return deleted > 0;
        } catch (Exception e) {
            throw new Throwable(e.getMessage());
        }
    }
}
