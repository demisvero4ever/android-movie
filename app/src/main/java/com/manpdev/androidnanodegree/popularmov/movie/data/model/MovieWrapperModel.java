package com.manpdev.androidnanodegree.popularmov.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieWrapperModel implements Parcelable{
    private int page;
    private List<MovieModel> results;


    protected MovieWrapperModel(Parcel in) {
        page = in.readInt();
        results = in.createTypedArrayList(MovieModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(results);
    }

    public int getPage() {
        return page;
    }

    public List<MovieModel> getResults() {
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieWrapperModel> CREATOR = new Creator<MovieWrapperModel>() {
        @Override
        public MovieWrapperModel createFromParcel(Parcel in) {
            return new MovieWrapperModel(in);
        }

        @Override
        public MovieWrapperModel[] newArray(int size) {
            return new MovieWrapperModel[size];
        }
    };
}
