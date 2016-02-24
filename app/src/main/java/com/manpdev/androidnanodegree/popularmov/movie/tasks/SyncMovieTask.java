package com.manpdev.androidnanodegree.popularmov.movie.tasks;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MoviesApi;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;
import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MoviesProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    public boolean syncData() {
        MovieApiRequester apiRequester = MovieApiRequester.getInstance(mContext);
        try {
            MovieWrapperModel model = apiRequester.getSortedMovieList(getSortingOption());

            if (model == null || model.getResults() == null) {
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

    @MoviesApi.SortingOptions
    private String getSortingOption() {
        SharedPreferences preferences = mContext.getSharedPreferences(Preferences.COLLECTION_NAME, Context.MODE_PRIVATE);
        switch (preferences.getString(Preferences.SELECTED_SORTING_OPTION, MoviesApi.SORT_POPULARITY_DESC)) {
            case MoviesApi.SORT_POPULARITY_DESC:
                return MoviesApi.SORT_POPULARITY_DESC;

            case MoviesApi.SORT_VOTE_AVERAGE_DESC:
                return MoviesApi.SORT_VOTE_AVERAGE_DESC;

            default:
                return MoviesApi.SORT_POPULARITY_DESC;
        }
    }

    private void cacheModel(MovieWrapperModel model) {

        Log.d(TAG, "cacheModel() called with: " + "model = [" + model + "]");

        if (model.getResults().size() == 0)
            return;

        ContentResolver resolver = mContext.getContentResolver();
        ArrayList<ContentProviderOperation> batch = new ArrayList<>();

        StringBuilder deleteSelectionArgBuilder = new StringBuilder();

        //to insert
        buildInsertOperations(model, batch, deleteSelectionArgBuilder);

        ContentProviderOperation.Builder deleteOperation =  ContentProviderOperation.newDelete(MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY));

        deleteOperation.withSelection(String.format(Locale.US, "%s NOT IN (%s)",
                MovieContract.MovieEntry.COLUMN_CLOUD_ID, deleteSelectionArgBuilder.toString()), null);

        batch.add(deleteOperation.build());

        try {
            resolver.applyBatch(MoviesProvider.sAUTHORITY, batch);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "cacheModel: ", e);
        }
    }

    private void buildInsertOperations(MovieWrapperModel model, ArrayList<ContentProviderOperation> batch, StringBuilder deleteSelectionArgBuilder) {

        ContentProviderOperation.Builder operation;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        MovieModel movie;

        for (int i = 0; i < model.getResults().size(); i++) {
            movie = model.getResults().get(i);

            operation = ContentProviderOperation.newInsert(MovieContract.MovieEntry.baseURI(MoviesProvider.sAUTHORITY));

            initInsertOperation(operation, dateFormat, movie);

            batch.add(operation.build());

            if(i == model.getResults().size() - 1)
                deleteSelectionArgBuilder.append(String.format(Locale.US, "%s", movie.getId()));
            else
                deleteSelectionArgBuilder.append(String.format(Locale.US, "%s, ", movie.getId()));
        }
    }

    private void initInsertOperation(ContentProviderOperation.Builder operation,
                                                                 SimpleDateFormat dateFormat, MovieModel movie) {

        operation.withValue(MovieContract.MovieEntry.COLUMN_CLOUD_ID, movie.getId());
        operation.withValue(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        operation.withValue(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        operation.withValue(MovieContract.MovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        operation.withValue(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        operation.withValue(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());

        try {
            operation.withValue(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, dateFormat.parse(movie.getReleaseDate()).getTime());
        } catch (ParseException e) {
            Log.w(TAG, String.format(Locale.US, "cacheModel: Invalid Movie Date: %s", movie.getReleaseDate()));
            operation.withValue(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, dateFormat.getCalendar().getTimeInMillis());
        }
    }
}
