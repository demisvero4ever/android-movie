package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView, OnMoviePosterClick{

    private MovieSelectionListener mSelectionListener;
    private MovieListContract.PopularMovieListPresenter mPresenter;

    private RecyclerView mList;
    private MovieListPosterAdapter mMovieListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mPresenter = new MovieList(this);
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

        mPresenter.loadMovieList(getLoaderManager());
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mSelectionListener = (MovieSelectionListener) context;
        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectionListener = null;
    }

    @Override
    public void showPosterList(List<MovieModel> list) {
        this.mMovieListAdapter = new MovieListPosterAdapter(getContext(), list, this);
        this.mList.swapAdapter(mMovieListAdapter, false);
    }

    @Override
    public void onMoviePosterSelected(int movieId) {
        if(mSelectionListener != null)
            mSelectionListener.onSelectMovie(movieId);
    }

    public interface MovieSelectionListener {
        void onSelectMovie(int id);
    }
}
