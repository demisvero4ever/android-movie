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
public class MovieExtras implements Parcelable{

    private int mMovieId;
    private List<MovieTrailerModel> mTrailers;
    private List<MovieReviewModel> mReviews;

    public MovieExtras(int movieId) {
        this.mMovieId = movieId;
        this.mTrailers = new ArrayList<>();
        this.mReviews = new ArrayList<>();
    }

    protected MovieExtras(Parcel in) {
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

    public static final Creator<MovieExtras> CREATOR = new Creator<MovieExtras>() {
        @Override
        public MovieExtras createFromParcel(Parcel in) {
            return new MovieExtras(in);
        }

        @Override
        public MovieExtras[] newArray(int size) {
            return new MovieExtras[size];
        }
    };
}
