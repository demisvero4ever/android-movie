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
    private String release_date;
    private double vote_average;
    private double popularity;

    public MovieModel() {}


    protected MovieModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeDouble(vote_average);
        dest.writeDouble(popularity);
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

    public String getPosterPath() {
        return poster_path;
    }

    public String getReleaseDate() {
        return release_date;
    }

    public double getVoteAverage() {
        return vote_average;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setReleaseDate(String release_date) {
        this.release_date = release_date;
    }

    public void setVoteAverage(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
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
