package com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper;

import android.os.Parcel;
import android.os.Parcelable;

import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * novoa on 3/22/16.
 */
public class MovieExtrasModel implements Parcelable{

    private int mMovieId;
    private List<MovieTrailerModel> mTrailers;
    private List<MovieReviewModel> mReviews;

    public MovieExtrasModel(int movieId) {
        this.mMovieId = movieId;
        this.mTrailers = new ArrayList<>();
        this.mReviews = new ArrayList<>();
    }

    protected MovieExtrasModel(Parcel in) {
        mMovieId = in.readInt();
        mTrailers = in.createTypedArrayList(MovieTrailerModel.CREATOR);
        mReviews = in.createTypedArrayList(MovieReviewModel.CREATOR);
    }

    public int getmMovieId() {
        return mMovieId;
    }

    public void setmMovieId(int mMovieId) {
        this.mMovieId = mMovieId;
    }

    public void setTrailers(List<MovieTrailerModel> mTrailers) {
        this.mTrailers = mTrailers;
    }

    public void setReviews(List<MovieReviewModel> mReviews) {
        this.mReviews = mReviews;
    }

    public List<MovieTrailerModel> getTrailers() {
        return mTrailers;
    }

    public List<MovieReviewModel> getReviews() {
        return mReviews;
    }

    public int getExtrasTotal(){
        return this.mReviews.size() + this.mTrailers.size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMovieId);
        dest.writeTypedList(mTrailers);
        dest.writeTypedList(mReviews);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieExtrasModel> CREATOR = new Creator<MovieExtrasModel>() {
        @Override
        public MovieExtrasModel createFromParcel(Parcel in) {
            return new MovieExtrasModel(in);
        }

        @Override
        public MovieExtrasModel[] newArray(int size) {
            return new MovieExtrasModel[size];
        }
    };
}
