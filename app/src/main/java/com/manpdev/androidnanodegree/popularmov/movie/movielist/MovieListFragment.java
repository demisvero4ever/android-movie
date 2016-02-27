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
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView,
        OnMoviePosterClick{

    private static final String TAG = "MovieListFragment";

    private MovieSelectionListener mSelectionListener;
    private MovieListContract.PopularMovieListPresenter mPresenter;

    private RecyclerView mList;
    private MovieListPosterAdapter mMovieListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = new MovieList(getActivity().getApplicationContext(), this, getLoaderManager());
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
        mSelectionListener = (MovieSelectionListener) getActivity();
        mPresenter.registerListeners();

        if(mList.getAdapter().getItemCount() == 0)
            mPresenter.loadMovieList();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSelectionListener = null;
        mPresenter.unregisterListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dismissMovieList();
        mPresenter = null;
    }

    @Override
    public void showMovieList(List<MovieModel> list) {
        if(list != null && list.size() > 0) {
            this.mMovieListAdapter.setmMovieList(list);
            this.mMovieListAdapter.notifyDataSetChanged();
            Log.d(TAG, "showMovieList: List Updated");
        }
    }

    @Override
    public void showMessage(int resourceId) {
        Snackbar.make(mList, getString(resourceId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMoviePosterSelected(View view, int movieId) {
        if(mSelectionListener != null)
            mSelectionListener.onSelectMovie(view, movieId);
    }
}
