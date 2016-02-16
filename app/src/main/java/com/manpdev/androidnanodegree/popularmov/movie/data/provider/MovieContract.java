package com.manpdev.androidnanodegree.popularmov.movie.data.provider;

import android.provider.BaseColumns;

/**
 * Created by novoa.pro@gmail.com on 2/16/16
 */
public class MovieContract {

    public static final class MovieEntry implements BaseColumns{

        public static final String TABLE_NAME = "pm_movie";

        public static final String COLUMN_CLOUD_ID = "cloud_id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_POSTER = "poster";

        public static final String COLUMN_VOTE_AVERAGE = "vote_avg";

        public static final String COLUMN_SYNOPSIS = "synopsis";

    }
}
