package com.manpdev.androidnanodegree.popularmov.movie;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by novoa.pro@gmail.com on 2/23/16
 */
public class Preferences {
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            BY_POPULARITY_DESC,
            BY_VOTE_AVERAGE_DESC,
            FAVORITES
    })
    public @interface SelectionOptions {
    }

    public final static String BY_POPULARITY_DESC = "popularity.desc";
    public final static String BY_VOTE_AVERAGE_DESC = "vote_average.desc";
    public final static String FAVORITES = "favorites";

    public final static String COLLECTION_NAME = "movies_preferences";

    public final static String MOVIE_SELECTION_OPTION = "movies_selection_option";
    private final static Object lock = new Object();
    private static SharedPreferences sPreferences;

    private static SharedPreferences getPreferences(Context context) {
        if (sPreferences == null)
            sPreferences = context.getSharedPreferences(Preferences.COLLECTION_NAME, Context.MODE_PRIVATE);

        return sPreferences;
    }

    @SelectionOptions
    public static String getSelectionOption(Context context) {
        switch (getPreferences(context).getString(Preferences.MOVIE_SELECTION_OPTION, BY_POPULARITY_DESC)) {
            case BY_POPULARITY_DESC:
                return BY_POPULARITY_DESC;

            case BY_VOTE_AVERAGE_DESC:
                return BY_VOTE_AVERAGE_DESC;

            case FAVORITES:
            default:
                return FAVORITES;
        }
    }

    public static void setSelectionOption(Context context, @SelectionOptions String selectionOption) {
        synchronized (lock) {
            getPreferences(context).edit().putString(MOVIE_SELECTION_OPTION, selectionOption).apply();
        }
    }

    public static void registerPreferencesListener(Context context,
                                                   SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPreferencesListener(Context context,
                                                     SharedPreferences.OnSharedPreferenceChangeListener listener) {
        getPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }


}
