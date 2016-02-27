package com.manpdev.androidnanodegree.popularmov.movie.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.movie.data.provider.MovieContract;

import org.junit.runner.RunWith;

import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.fail;

/**
 * Created by novoa.pro@gmail.com on 2/16/16
 */
@RunWith(AndroidJUnit4.class)
public class MovieDatabaseTest {

    private SQLiteDatabase database;
    private static final String[] MOVIE_PROJECTION = new String[]{
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_CLOUD_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER
    };

    @org.junit.Before
    public void setUp() throws Exception {
        MovieDatabase helper = new MovieDatabase(InstrumentationRegistry.getTargetContext());
        this.database = helper.getWritableDatabase();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        if (this.database != null && this.database.isOpen())
            this.database.close();
    }

    @org.junit.Test
    public void testInsertRow() throws Exception {
        this.database.beginTransaction();

        ContentValues movieContent = getFakeMovieData();

        long id = database.insert(MovieContract.MovieEntry.TABLE_NAME,
                null, movieContent);

        assertNotSame(0, id);

        this.database.endTransaction();
    }

    @org.junit.Test
    public void testQueryRow() throws Exception {
        try {

            this.database.beginTransaction();

            ContentValues movieContent = getFakeMovieData();

            long id = database.insert(MovieContract.MovieEntry.TABLE_NAME,
                    null, movieContent);

            assertNotSame(0, id);

            Cursor cursor = this.database.query(false, MovieContract.MovieEntry.TABLE_NAME,
                    MOVIE_PROJECTION,
                    String.format(Locale.US, "%s = %s", MovieContract.MovieEntry.COLUMN_CLOUD_ID,
                            getFakeMovieData().getAsString(MovieContract.MovieEntry.COLUMN_CLOUD_ID)),
                    null,
                    null,
                    null,
                    null,
                    null
            );

            assertEquals(true, cursor.moveToFirst());
            assertEquals(getFakeMovieData().getAsString(MovieContract.MovieEntry.COLUMN_POSTER),
                    cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)));

            cursor.close();
        }catch (Exception ex){
            fail(ex.getMessage());
        }
        finally {
            this.database.endTransaction();
        }
    }

    @NonNull
    private ContentValues getFakeMovieData() {
        ContentValues movieContent = new ContentValues();
        movieContent.put(MovieContract.MovieEntry.COLUMN_CLOUD_ID, 1);
        movieContent.put(MovieContract.MovieEntry.COLUMN_POSTER, "poster.jpg");
        movieContent.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, 1234567890);
        movieContent.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, "This is the synopsis");
        movieContent.put(MovieContract.MovieEntry.COLUMN_TITLE, "This is a title");
        movieContent.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, 6.7);
        movieContent.put(MovieContract.MovieEntry.COLUMN_POPULARITY, 6.7);
        return movieContent;
    }
}