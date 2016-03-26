package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private String mTrailerId;
    private MenuItem mShareMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        if(getArguments() != null){
            this.mMovie = getArguments().getParcelable(MovieSelectionListener.EXTRA_MOVIE);
        }else if(savedInstanceState != null) {
            this.mMovie = savedInstanceState.getParcelable(MOVIE_STATE);
        }
        mPresenter = new MovieDetails(getContext(), this);
        mMovieAdapter = new MovieDetailsAdapter(mMovie, this);
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
        mMovieDetailList.setAdapter(this.mMovieAdapter);

        if (mMovie != null && !mTwoPanels)
            updateMovie(mMovie);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        mShareMenuItem =
                menu.add(Menu.NONE, R.id.share_trailer, 10, R.string.share_label);
        mShareMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        mShareMenuItem.setIcon(R.drawable.ic_share_white_24dp);
        mShareMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                shareTrailer();
                return true;
            }
        });
        mShareMenuItem.setVisible(false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.removeCallBacks();
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
        mMovieAdapter.setMovie(mMovie);
        mMovieAdapter.notifyDataSetChanged();
    }

    @Override
    public void enableTrailerSharing(String trailerId) {
        mTrailerId = trailerId;
        mShareMenuItem.setVisible(true);
    }

    @Override
    public void disableTrailerSharing() {
        mTrailerId = null;

        if(mShareMenuItem != null)
            mShareMenuItem.setVisible(false);
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
        startActivity(intent);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void openWebReview(String url) {
        try {
            CustomTabsIntent.Builder tabBuilder = new CustomTabsIntent.Builder();
            tabBuilder.enableUrlBarHiding()
                    .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                    .setSecondaryToolbarColor(getResources().getColor(R.color.colorPrimaryDark));

            CustomTabsIntent tabIntent = tabBuilder.build();
            tabIntent.launchUrl(getActivity(), Uri.parse(url));
        }catch (Exception ex){
            //if not supported old browser
            Intent intent  = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

    private void shareTrailer(){
        Intent toShare = new Intent(Intent.ACTION_SEND);
        toShare.putExtra(Intent.EXTRA_TEXT, getString(R.string.youtube_video_base_url) + mTrailerId);
        toShare.setType("text/plain");
        startActivity(toShare);
    }
}

