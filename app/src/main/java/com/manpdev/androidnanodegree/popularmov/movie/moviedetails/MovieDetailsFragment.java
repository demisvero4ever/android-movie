package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieExtrasModel;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.MovieDetailsAdapter;
import com.manpdev.androidnanodegree.popularmov.movie.moviedetails.adapters.MovieDetailsListener;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieListActivity;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.MovieSelectionListener;

public class MovieDetailsFragment extends Fragment implements MovieDetailsContract.MovieDetailsView, MovieDetailsListener{

    private static final String TAG = "MovieDetailsFragment";
    private static final String MOVIE_STATE = "::movie_state";

    private MovieModel mMovie;
    private MovieExtrasModel mMovieExtras;
    private boolean mTwoPanels;

    private MovieDetailsAdapter mMovieAdapter;

    private MovieDetailsContract.MovieDetailsPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            this.mMovie = getArguments().getParcelable(MovieSelectionListener.EXTRA_MOVIE);
        }else if(savedInstanceState != null){
            this.mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
        }

        if(this.mMovie != null)
            mMovieAdapter = new MovieDetailsAdapter(mMovie, this);

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

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        RecyclerView mMovieDetailList = (RecyclerView) root.findViewById(R.id.rv_movie_details);
        mMovieDetailList.setHasFixedSize(true);
        mMovieDetailList.setLayoutManager(llm);

        if(this.mMovieAdapter != null)
            mMovieDetailList.setAdapter(this.mMovieAdapter);

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
    public void showMovieExtras(MovieExtrasModel extras) {
        Log.d(TAG, "showMovieExtras() called with: " + "extras = [" + extras + "]");
        mMovieExtras = extras;
        mMovieAdapter.setMovieExtras(mMovieExtras);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void favoriteSelection(boolean isFavorite) {
        mMovie.setFavorite(isFavorite);
        mMovieAdapter.updateMovie(mMovie);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void setMovieAsFavorite() {
        mPresenter.saveMovieAsFavorite(mMovie);
    }

    @Override
    public void removeMovieFromFavorites() {
        mPresenter.removeMovieFromFavorites(mMovie.getId());
    }

    @Override
    public void openTrailerVideo(String videoId) {
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.youtube_video_base_url) + videoId));
        getContext().startActivity(intent);
    }

    @Override
    public void openWebReview(String url) {
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        getContext().startActivity(intent);
    }
}

