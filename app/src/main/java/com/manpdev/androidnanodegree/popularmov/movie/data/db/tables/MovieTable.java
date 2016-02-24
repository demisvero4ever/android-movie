package com.manpdev.androidnanodegree.popularmov.movie.data.db.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;

/**
 * Created by novoa.pro@gmail.com on 2/16/16
 */
public class MovieTable {

    private static final String TAG = "MovieTable";

    private static final String CREATE_TABLE_SQL_V1 = "CREATE TABLE " +
            MovieContract.MovieEntry.TABLE_NAME + " ( " +
            MovieContract.MovieEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
            MovieContract.MovieEntry.COLUMN_CLOUD_ID + " NUMERIC NOT NULL UNIQUE, " +
            MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " NUMERIC NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POSTER + " TEXT, " +
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
            MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL " + ")";

    private static final String SCRIPT_CREATE_INDEX_1 =
            "CREATE INDEX MOVIE_ID_INDX on " + MovieContract.MovieEntry.TABLE_NAME +
                    " (" +
                    MovieContract.MovieEntry.COLUMN_CLOUD_ID +
                    ")";

    private static final String SCRIPT_CREATE_INDEX_2 =
            "CREATE INDEX MOVIE_POPULARITY_INDX on " + MovieContract.MovieEntry.TABLE_NAME +
                    " (" +
                    MovieContract.MovieEntry.COLUMN_POPULARITY +
                    ")";

    private static final String SCRIPT_CREATE_INDEX_3 =
            "CREATE INDEX MOVIE_VOTE_AVG_INDX on " + MovieContract.MovieEntry.TABLE_NAME +
                    " (" +
                    MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE +
                    ")";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL_V1);
        db.execSQL(SCRIPT_CREATE_INDEX_1);
        db.execSQL(SCRIPT_CREATE_INDEX_2);
        db.execSQL(SCRIPT_CREATE_INDEX_3);
    }

    public static void updateTable(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (db == null || oldVersion == newVersion)
            return;

        Log.d(TAG, String.format("updateTable: oldVersion: %d/ newVersion: %d.", oldVersion, newVersion));
    }

}
