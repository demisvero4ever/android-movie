package com.manpdev.androidnanodegree.popularmov.movie.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by novoa.pro@gmail.com on 2/14/16
 */
public class MovieModel implements Parcelable {

    private static final String sPosterBasePath = "http://image.tmdb.org/t/p/w185";
    public static String RElEASE_DATE_FORMAT = "yyyy-MM-dd";

    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private String release_date;
    private double vote_average;
    private double popularity;
    private boolean favorite;

    public MovieModel() {
    }

    protected MovieModel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
        favorite = in.readByte() != 0;
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
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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
        if (!TextUtils.isEmpty(poster_path))
            return sPosterBasePath + poster_path;
        else
            return poster_path;
    }

    public String getPoster() {
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

    public String getReleaseYear() {
        if (TextUtils.isEmpty(release_date))
            return "";

        DateFormat df = new SimpleDateFormat(MovieModel.RElEASE_DATE_FORMAT, Locale.US);
        Calendar c = Calendar.getInstance();

        try {
            c.setTime(df.parse(release_date));
            return String.valueOf(c.get(Calendar.YEAR));

        } catch (ParseException e) {
            return "";
        }
    }

}
