package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieReviewModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieTrailerModel;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieListActivity;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieSelectionListener;

import java.util.List;

public class MovieDetailsFragment extends Fragment implements MovieDetailsContract.MovieDetailsView{

    private static final String TAG = "MovieDetailsFragment";
    private static final String MOVIE_STATE = "::movie_state";

    private MovieModel mMovie;
    private boolean mTwoPanels;

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
        Log.d(TAG, "updateMovie() called with: " + "movie = [" + movie + "]");
        this.mMovie = movie;
        mPresenter.loadMovieDetails(mMovie.getId());
    }

    @Override
    public void showMovieTrailers(List<MovieTrailerModel> trailers) {

    }

    @Override
    public void showMovieReviews(List<MovieReviewModel> trailers) {

    }

    @Override
    public void favoriteSelection(boolean isFavorite) {

    }
}

