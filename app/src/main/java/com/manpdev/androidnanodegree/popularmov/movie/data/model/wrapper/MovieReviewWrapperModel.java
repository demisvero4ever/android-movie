package com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;

import java.util.List;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieReviewWrapperModel implements Parcelable{

    private List<MovieReviewModel> results;

    protected MovieReviewWrapperModel(Parcel in) {
        results = in.createTypedArrayList(MovieReviewModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
    }


    public List<MovieReviewModel> getResults() {
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieReviewWrapperModel> CREATOR = new Creator<MovieReviewWrapperModel>() {
        @Override
        public MovieReviewWrapperModel createFromParcel(Parcel in) {
            return new MovieReviewWrapperModel(in);
        }

        @Override
        public MovieReviewWrapperModel[] newArray(int size) {
            return new MovieReviewWrapperModel[size];
        }
    };
}
