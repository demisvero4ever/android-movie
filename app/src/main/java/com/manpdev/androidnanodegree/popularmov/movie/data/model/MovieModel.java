package com.manpdev.androidnanodegree.popularmov.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieModel implements Parcelable{

    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private boolean video;
    private double vote_average;

    public MovieModel() {}


    protected MovieModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
        video = in.readByte() != 0;
        vote_average = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(release_date);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeDouble(vote_average);
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}
