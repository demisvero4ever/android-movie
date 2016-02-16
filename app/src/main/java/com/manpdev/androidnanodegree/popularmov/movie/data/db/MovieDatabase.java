package com.manpdev.androidnanodegree.popularmov.movie.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manpdev.androidnanodegree.popularmov.movie.data.db.tables.MovieTable;

/**
 * Created by novoa.pro@gmail.com on 2/16/16
 */
public class MovieDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";
    private static final int DB_VERSION = 1;
    private static final String PRAGMA_FOREIGN_KEYS_ON = "PRAGMA foreign_keys=ON; ";

    public MovieDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MovieTable.createTable(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL(PRAGMA_FOREIGN_KEYS_ON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MovieTable.updateTable(db, oldVersion, newVersion);
    }

}
