package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieListActivity;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieSelectionListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class MovieDetailsFragment extends Fragment implements MovieDetailsContract.MovieDetailsView{

    private static final String TAG = "MovieDetailsFragment";
    private static final String MOVIE_STATE = "::movie_state";

    private MovieModel mMovie;
    private boolean mTwoPanels;

    private ViewGroup mTitleContainer;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mVoteAvgTextView;
    private TextView mDateTextView;
    private ToggleButton mFavoriteToggle;

    private MovieDetailsContract.MovieDetailsPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.mMovie = getArguments().getParcelable(MovieSelectionListener.EXTRA_MOVIE);
        }else if(savedInstanceState != null){
            this.mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
        }

        mPresenter = new MovieDetails(getContext(), this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MovieListActivity)
            mTwoPanels = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);

        this.mTitleContainer = (ViewGroup) root.findViewById(R.id.fl_title_container);
        this.mPosterImageView = (ImageView) root.findViewById(R.id.iv_movie_poster);
        this.mTitleTextView = (TextView) root.findViewById(R.id.tv_movie_title);
        this.mSynopsisTextView = (TextView) root.findViewById(R.id.tv_movie_synopsis);
        this.mVoteAvgTextView = (TextView) root.findViewById(R.id.tv_movie_vote_avg);
        this.mDateTextView = (TextView) root.findViewById(R.id.tv_movie_release_date);
        this.mFavoriteToggle = (ToggleButton) root.findViewById(R.id.bt_favorite);

        this.mFavoriteToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoriteToggle.isChecked())
                    mPresenter.saveMovieAsFavorite(mMovie);
                else
                    mPresenter.removeMovieFromFavorites(mMovie.getId());
            }
        });

        if (mMovie != null && !mTwoPanels)
            updateMovie(mMovie);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMovie != null)
            outState.putParcelable(MOVIE_STATE, mMovie);
    }

    public void updateMovie(MovieModel movie){
        this.mMovie = movie;
        mPresenter.loadMovieDetails(mMovie.getId());
        showMovieDetails();
    }

    @Override
    public void showMovieTrailers(List<MovieTrailerModel> trailers) {

    }

    @Override
    public void showMovieReviews(List<MovieReviewModel> trailers) {

    }

    @Override
    public void favoriteSelection(boolean isFavorite) {
        this.mFavoriteToggle.setVisibility(View.VISIBLE);
        this.mFavoriteToggle.setChecked(isFavorite);
    }

    private void showMovieDetails() {
        Log.d(TAG, "showMovieDetails() called with: " + "movie = [" + mMovie.getTitle() + "]");

        this.mTitleContainer.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(mMovie.getPosterPath()))
            Picasso.with(getContext())
                    .load(mMovie.getPosterPath())
                    .error(R.drawable.ic_no_poster_available)
                    .into(mPosterImageView);
        else
            Picasso.with(getContext())
                    .load(R.drawable.ic_no_poster_available)
                    .into(mPosterImageView);

        mTitleTextView.setText(mMovie.getTitle());
        mSynopsisTextView.setText(mMovie.getOverview());
        mVoteAvgTextView.setText(String.format(Locale.US, "%.2f/10", mMovie.getVoteAverage()));
        mDateTextView.setText(mMovie.getReleaseYear());
    }
}

