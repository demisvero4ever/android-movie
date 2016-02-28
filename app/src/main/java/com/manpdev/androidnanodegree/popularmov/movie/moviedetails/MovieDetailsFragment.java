package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieSelectionListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class MovieDetailsFragment extends Fragment implements MovieDetailsContract.MovieDetailsView {

    private static final String TAG = "MovieDetailsFragment";

    private MovieDetailsContract.MovieDetailsPresenter mPresenter;

    private int mMovieId;
    private MovieModel mMovie;

    private ViewGroup mTitleContainer;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mSynopsisTextView;
    private TextView mVoteAvgTextView;
    private TextView mDateTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new MovieDetails(getActivity().getApplicationContext(), this, getLoaderManager());

        this.mMovieId = getArguments().getInt(MovieSelectionListener.EXTRA_MOVIE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_details, container, false);

        this.mTitleContainer = (ViewGroup) root.findViewById(R.id.fl_title_container);
        this.mTitleContainer.setY(-1 * getResources().getDimension(R.dimen.main_title_frame_height));
        this.mPosterImageView = (ImageView) root.findViewById(R.id.iv_movie_poster);
        this.mTitleTextView = (TextView) root.findViewById(R.id.tv_movie_title);
        this.mSynopsisTextView = (TextView) root.findViewById(R.id.tv_movie_synopsis);
        this.mVoteAvgTextView = (TextView) root.findViewById(R.id.tv_movie_vote_avg);
        this.mDateTextView = (TextView) root.findViewById(R.id.tv_movie_release_date);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            initTransitionElements();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mMovie == null)
            mPresenter.loadMovieDetails(this.mMovieId);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPresenter.dismissMovieDetails();
        super.onDestroy();
    }

    @Override
    public void showMovieDetails(MovieModel movie) {
        Log.d(TAG, "showMovieDetails() called with: " + "movie = [" + movie.getTitle() + "]");

        this.mMovie = movie;
        this.mTitleContainer.setVisibility(View.VISIBLE);
        this.mTitleContainer.animate()
                .setInterpolator(new AccelerateInterpolator())
                .translationY(0)
                .setStartDelay(20)
                .setListener(null);

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
        mDateTextView.setText(mMovie.getReleaseDate());
    }

    private void initTransitionElements() {
        ViewCompat.setTransitionName(this.mPosterImageView, getString(R.string.movie_poster_resource));
    }
}

