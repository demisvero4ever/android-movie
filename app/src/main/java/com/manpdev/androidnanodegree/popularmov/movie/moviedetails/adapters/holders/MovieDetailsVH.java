package com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.Locale;

/**
 * novoa on 3/22/16.
 */
public class MovieDetailsVH extends RecyclerView.ViewHolder{

    private ViewGroup mTitleContainer;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mVoteAvgTextView;
    private TextView mDateTextView;

    public MovieDetailsVH(View itemView) {
        super(itemView);
        this.mTitleContainer = (ViewGroup) itemView.findViewById(R.id.fl_title_container);
        this.mPosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        this.mTitleTextView = (TextView) itemView.findViewById(R.id.tv_movie_title);
        this.mSynopsisTextView = (TextView) itemView.findViewById(R.id.tv_movie_synopsis);
        this.mVoteAvgTextView = (TextView) itemView.findViewById(R.id.tv_movie_vote_avg);
        this.mDateTextView = (TextView) itemView.findViewById(R.id.tv_movie_release_date);
    }

    public void bindContent(MovieModel movie){
        this.mTitleContainer.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(movie.getPosterPath()))
            Picasso.with(mPosterImageView.getContext())
                    .load(movie.getPosterPath())
                    .error(R.drawable.ic_no_poster_available)
                    .into(mPosterImageView);
        else
            Picasso.with(mPosterImageView.getContext())
                    .load(R.drawable.ic_no_poster_available)
                    .into(mPosterImageView);

        mTitleTextView.setText(movie.getTitle());
        mSynopsisTextView.setText(movie.getOverview());
        mVoteAvgTextView.setText(String.format(Locale.US, "%.2f/10", movie.getVoteAverage()));
        mDateTextView.setText(movie.getReleaseYear());
    }
}
