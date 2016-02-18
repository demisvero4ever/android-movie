package com.manpdev.androidnanodegree.popularmov.movie.data.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by novoa.pro@gmail.com on 2/16/16
 */
public class MovieContract {

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_CLOUD_ID = "cloud_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE_AVERAGE = "vote_avg";
        public static final String COLUMN_SYNOPSIS = "synopsis";

        public static String[] COLUMNS_ALL = {_ID, COLUMN_CLOUD_ID, COLUMN_TITLE,
                COLUMN_POSTER, COLUMN_SYNOPSIS, COLUMN_VOTE_AVERAGE, COLUMN_RELEASE_DATE};

        public static final String CONTENT_TYPE = getDirBaseType(TABLE_NAME);
        public static final String CONTENT_TYPE_ITEM = getItemBaseType(TABLE_NAME);

        public static Uri baseURI(String authority){
            return getBaseContentUri(authority, TABLE_NAME);
        }

        public static Uri buildUriForMovieById(String authority, int movieId){
            return Uri.withAppendedPath(baseURI(authority), String.valueOf(movieId));
        }
    }

    private static Uri getBaseContentUri(String authority, String basePath) {
        return Uri.parse(ContentResolver.SCHEME_CONTENT + "://" + authority + "/" + basePath);
    }

    private static String getDirBaseType(String type) {
        return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + type;
    }

    private static String getItemBaseType(String type) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + type;
    }
}
