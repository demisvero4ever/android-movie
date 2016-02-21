package com.manpdev.androidnanodegree.popularmov.movie.tasks;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.WorkerThread;
import android.text.format.DateFormat;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by novoa.pro@gmail.com on 2/21/16
 */
public class SyncMovieTask {

    private static final String TAG = "SyncMovieTask";

    private final Context mContext;
    public static final int TASK_ID = 1;

    public SyncMovieTask(Context mContext) {
        this.mContext = mContext;
    }

    @WorkerThread
    public boolean syncData(){
        MovieApiRequester apiRequester = MovieApiRequester.getInstance(mContext);
        try {
            MovieWrapperModel model = apiRequester.getMovieList(1);

            if(model == null || model.getResults() == null) {
                Log.w(TAG, "syncData: Null model");
                return false;
            }

            cacheModel(model);

            return true;
        } catch (Throwable e) {
            Log.e(TAG, "syncData: ", e);
            return false;
        }
    }

    private void cacheModel(MovieWrapperModel model) {

        Log.d(TAG, "cacheModel() called with: " + "model = [" + model + "]");

        if(model.getResults().size() == 0)
            return;

        ContentResolver resolver = mContext.getContentResolver();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        ContentValues[] valueList = new ContentValues[model.getResults().size()];
        ContentValues values;
        MovieModel movie;

        for(int i= 0; i< model.getResults().size(); i++){
            movie= model.getResults().get(i);

            values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_CLOUD_ID, movie.getId());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            try {
                values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, dateFormat.parse(movie.getReleaseDate()).getTime());
            } catch (ParseException e) {
                Log.w(TAG, String.format(Locale.US, "cacheModel: Invalid Movie Date: %s", movie.getReleaseDate()));
                values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, dateFormat.getCalendar().getTimeInMillis());
            }
            valueList[i] = values;
        }

        resolver.bulkInsert(MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY), valueList);
    }
}
