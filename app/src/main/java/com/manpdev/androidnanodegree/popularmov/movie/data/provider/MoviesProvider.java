package com.manpdev.androidnanodegree.popularmov.movie.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.movie.data.db.MovieDatabase;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MoviesProvider extends ContentProvider{

    private static final String TAG = "MoviesProvider";
    public static final String sAUTHORITY = "com.manpdev.androidnanodegree.popularmov.movie";

    private static final int URI_MATCH_MOVIE = 0;
    private static final int URI_MATCH_MOVIE_ID = 1;

    private static UriMatcher sUriMatcher;
    private MovieDatabase mMovieDatabase;

    @Override
    public boolean onCreate() {
        initUriMatcher();
        this.mMovieDatabase = new MovieDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mMovieDatabase.getReadableDatabase();
        Cursor result;

        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                result = database.query(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMNS_ALL,
                                        null, null, null, null, null);
                break;

            case URI_MATCH_MOVIE_ID:
                result = database.query(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMNS_ALL,
                        MovieContract.MovieEntry._ID + "=? ", new String[]{uri.getLastPathSegment()}, null, null, null);
                break;
            default:
                String message = "Unknown URI: " + uri.toString();
                Log.e(TAG, message);
                throw new IllegalArgumentException(message);
        }

        return result;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase database = mMovieDatabase.getReadableDatabase();

        String tableName;

        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                tableName = MovieContract.MovieEntry.TABLE_NAME;
                break;

            default:
                String message = "Unknown URI: " + uri.toString();
                Log.e(TAG, message);
                throw new IllegalArgumentException(message);
        }

        long resultId = database.insertWithOnConflict(
                tableName,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
        );

        return ContentUris.withAppendedId(uri, resultId);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mMovieDatabase.getWritableDatabase();
        int result;

        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                result = database.delete(MovieContract.MovieEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;

            case URI_MATCH_MOVIE_ID:
                result = database.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry._ID + "=? ", new String[]{uri.getLastPathSegment()});
                break;
            default:
                String message = "Unknown URI: " + uri.toString();
                Log.e(TAG, message);
                throw new IllegalArgumentException(message);
        }

        if(getContext() != null && result != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mMovieDatabase.getWritableDatabase();
        int result;

        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                result = database.update(MovieContract.MovieEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case URI_MATCH_MOVIE_ID:
                result = database.update(MovieContract.MovieEntry.TABLE_NAME, values,
                        MovieContract.MovieEntry._ID + "=? ", new String[]{uri.getLastPathSegment()});
                break;
            default:
                String message = "Unknown URI: " + uri.toString();
                Log.e(TAG, message);
                throw new IllegalArgumentException(message);
        }

        if(getContext() != null && result != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case URI_MATCH_MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_TYPE_ITEM;
            default:
                return null;
        }
    }

    private void initUriMatcher() {
        if(sUriMatcher != null)
            return;

        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //../movie
        sUriMatcher.addURI(sAUTHORITY, MovieContract.MovieEntry.TABLE_NAME, URI_MATCH_MOVIE);
        //../movie/{movie_id}
        sUriMatcher.addURI(sAUTHORITY, MovieContract.MovieEntry.TABLE_NAME + "/*", URI_MATCH_MOVIE_ID);
    }
}
