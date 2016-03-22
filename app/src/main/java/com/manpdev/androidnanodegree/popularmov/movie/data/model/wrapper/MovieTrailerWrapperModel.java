package com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieTrailerWrapperModel implements Parcelable{

    private List<MovieTrailerModel> results;

    public MovieTrailerWrapperModel() {
        this.results = new ArrayList<>();
    }

    protected MovieTrailerWrapperModel(Parcel in) {
        results = in.createTypedArrayList(MovieTrailerModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }


    public List<MovieTrailerModel> getResults() {
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieTrailerWrapperModel> CREATOR = new Creator<MovieTrailerWrapperModel>() {
        @Override
        public MovieTrailerWrapperModel createFromParcel(Parcel in) {
            return new MovieTrailerWrapperModel(in);
        }

        @Override
        public MovieTrailerWrapperModel[] newArray(int size) {
            return new MovieTrailerWrapperModel[size];
        }
    };
}
