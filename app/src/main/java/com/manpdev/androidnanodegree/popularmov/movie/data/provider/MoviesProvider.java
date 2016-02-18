package com.manpdev.androidnanodegree.popularmov.movie.data.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
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

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mMovieDatabase.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:

                break;

            case URI_MATCH_MOVIE_ID:

                break;
            default:
                String message = "Unknown URI: " + uri.toString();
                Log.e(TAG, message);
                throw new IllegalArgumentException(message);
        }

        
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case URI_MATCH_MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case URI_MATCH_MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_TYPE_ITEM;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
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
