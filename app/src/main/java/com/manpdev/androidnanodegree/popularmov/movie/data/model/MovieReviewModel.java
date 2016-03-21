package com.manpdev.androidnanodegree.popularmov.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * novoa on 3/20/16.
 */
public class MovieReviewModel implements Parcelable{
    private String author;
    private String content;
    private String url;

    protected MovieReviewModel(Parcel in) {
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieReviewModel> CREATOR = new Creator<MovieReviewModel>() {
        @Override
        public MovieReviewModel createFromParcel(Parcel in) {
            return new MovieReviewModel(in);
        }

        @Override
        public MovieReviewModel[] newArray(int size) {
            return new MovieReviewModel[size];
        }
    };
}
