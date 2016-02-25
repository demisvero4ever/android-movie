package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView,
        OnMoviePosterClick, SharedPreferences.OnSharedPreferenceChangeListener{

    private MovieSelectionListener mSelectionListener;
    private MovieListContract.PopularMovieListPresenter mPresenter;

    private RecyclerView mList;
    private MovieListPosterAdapter mMovieListAdapter;

    private boolean mLoadedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new MovieList(this, getLoaderManager());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_movie_list, container, false);

        this.mList = (RecyclerView) root.findViewById(R.id.rv_movie_posters);
        this.mList.setHasFixedSize(true);

        this.mList.setLayoutManager(new GridLayoutManager(getContext(),
                getResources().getInteger(R.integer.movie_list_columns)));

        this.mMovieListAdapter = new MovieListPosterAdapter(getContext(), new ArrayList<MovieModel>(), this);
        this.mList.setAdapter(this.mMovieListAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!this.mLoadedData)
            mPresenter.loadMovieList();

        Preferences.registerPreferencesListener(getContext(), this);

        mSelectionListener = (MovieSelectionListener) getActivity();
        mPresenter.registerSyncDataListener();
    }

    @Override
    public void onStop() {
        super.onStop();

        Preferences.unregisterPreferencesListener(getContext(), this);

        mSelectionListener = null;
        mPresenter.unregisterSyncDataListener();
    }

    @Override
    public void showMovieList(List<MovieModel> list) {
        if(list != null && list.size() > 0){
            this.mLoadedData = true;
            this.mMovieListAdapter.setmMovieList(list);
            this.mMovieListAdapter.notifyDataSetChanged();
        }else{
            mPresenter.startSyncData();
        }
    }

    @Override
    public void showMessage(int resourceId) {
        Snackbar.make(mList, getString(resourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMoviePosterSelected(int movieId) {
        if(mSelectionListener != null)
            mSelectionListener.onSelectMovie(movieId);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(Preferences.SELECTED_SORTING_OPTION))
            mPresenter.startSyncData();
    }
}
