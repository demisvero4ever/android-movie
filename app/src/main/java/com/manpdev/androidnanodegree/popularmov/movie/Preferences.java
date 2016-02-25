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
            SORT_POPULARITY_DESC,
            SORT_VOTE_AVERAGE_DESC
    })
    public @interface SortingOptions {}
    public final static String SORT_POPULARITY_DESC = "popularity.desc";
    public final static String SORT_VOTE_AVERAGE_DESC = "vote_average.desc";


    public final static String COLLECTION_NAME = "movies_preferences";

    public final static String SELECTED_SORTING_OPTION = "selected_sorting_option";
    private final static Object lock  = new Object();
    private static SharedPreferences sPreferences;

    private static SharedPreferences getPreferences(Context context){
        if(sPreferences == null)
            sPreferences = context.getSharedPreferences(Preferences.COLLECTION_NAME, Context.MODE_PRIVATE);

        return sPreferences;
    }

    @SortingOptions
    public static String getSortingOption(Context context) {
         switch (getPreferences(context).getString(Preferences.SELECTED_SORTING_OPTION, SORT_POPULARITY_DESC)) {
            case SORT_POPULARITY_DESC:
                return SORT_POPULARITY_DESC;

            case SORT_VOTE_AVERAGE_DESC:
                return SORT_VOTE_AVERAGE_DESC;

            default:
                return SORT_POPULARITY_DESC;
        }
    }

    public static void setSortingOption(Context context, @SortingOptions String sortingOption) {
        synchronized (lock) {
            getPreferences(context).edit().putString(SELECTED_SORTING_OPTION, sortingOption).apply();
        }
    }

    public static void registerPreferencesListener(Context context,
                                                   SharedPreferences.OnSharedPreferenceChangeListener listener){
        getPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPreferencesListener(Context context,
                                                   SharedPreferences.OnSharedPreferenceChangeListener listener){
        getPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }


}
