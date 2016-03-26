package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.adapters.MovieListPosterAdapter;
import com.manpdev.androidnanodegree.popularmov.movie.movielist.adapters.OnMoviePosterClick;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView,
        OnMoviePosterClick {

    private static final String TAG = "MovieListFragment";
    private static final String MOVIE_LIST_STATE_KEY = "::state_movie_list";

    private MovieSelectionListener mSelectionListener;
    private MovieListContract.PopularMovieListPresenter mPresenter;

    private RecyclerView mList;
    private MovieListPosterAdapter mMovieListAdapter;

    private List<MovieModel> mMovieList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new MovieList(getActivity().getApplicationContext(), this, getLoaderManager());

        if (savedInstanceState != null)
            this.mMovieList = savedInstanceState.getParcelableArrayList(MOVIE_LIST_STATE_KEY);
        else
            this.mMovieList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);

        this.mList = (RecyclerView) root.findViewById(R.id.rv_movie_posters);
        this.mList.setHasFixedSize(true);

        GridLayoutManager gridLayout = new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.movie_list_columns));

        gridLayout.setAutoMeasureEnabled(true);
        this.mList.setLayoutManager(gridLayout);

        this.mMovieListAdapter = new MovieListPosterAdapter(getContext(), mMovieList, this);
        this.mList.setAdapter(this.mMovieListAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSelectionListener = (MovieSelectionListener) getActivity();
        mPresenter.register();

        if (mMovieList.size() == 0 || Preferences.getSelectionOption(getContext()).equals(Preferences.FAVORITES))
            mPresenter.loadMovieList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mMovieList != null)
            outState.putParcelableArrayList(MOVIE_LIST_STATE_KEY, (ArrayList<MovieModel>) mMovieList);
    }

    @Override
    public void onStop() {
        super.onStop();
        mSelectionListener = null;
        mPresenter.unregister();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter = null;
    }

    @Override
    public void showMovieList(List<MovieModel> list) {
        mMovieList = list;

        if(mMovieList.size() == 0)
            mSelectionListener.clearSelection();
        else
            mSelectionListener.refreshDetails(list.get(0));

        this.mMovieListAdapter.setmMovieList(mMovieList);
        this.mMovieListAdapter.notifyDataSetChanged();
        Log.d(TAG, "showMovieList: List Updated");
    }

    @Override
    public void showMessage(int resourceId) {
        if(!this.isDetached())
            Snackbar.make(mList, getString(resourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMoviePosterSelected(MovieListPosterAdapter.PosterViewHolder holder) {
        if (mSelectionListener != null && mMovieList.size() > holder.getAdapterPosition()) {
            mSelectionListener.onSelectMovie(mMovieList.get(holder.getAdapterPosition()));
        }
    }

    public void doRefresh() {
        Log.d(TAG, "doRefresh");
        mPresenter.loadMovieList();
    }

}
