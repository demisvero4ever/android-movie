package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.tasks.SyncMovieTask;
import com.manpdev.androidnanodegree.popularmov.services.SyncDataService;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView, OnMoviePosterClick{

    private MovieSelectionListener mSelectionListener;
    private MovieListContract.PopularMovieListPresenter mPresenter;

    private RecyclerView mList;
    private MovieListPosterAdapter mMovieListAdapter;

    private final static int sListenerId = 123;
    private BroadcastReceiver mSyncReceiver;
    private boolean mLoadedData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SyncDataService.startSyncData(getActivity().getApplicationContext(), SyncMovieTask.TASK_ID, sListenerId);
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

            IntentFilter filter = new IntentFilter();
            filter.addAction(SyncDataService.ACTION_SYNC_COMPLETED);
            this.mSyncReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(!mLoadedData && mPresenter != null)
                        mPresenter.refreshMovieList(MovieListFragment.this.getLoaderManager());
                }
            };

            context.registerReceiver(mSyncReceiver, filter);

        }catch (Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectionListener = null;
        getActivity().unregisterReceiver(mSyncReceiver);
    }

    @Override
    public void showPosterList(List<MovieModel> list) {
        if(list != null && list.size() > 0){
            mLoadedData = true;
            getActivity().unregisterReceiver(mSyncReceiver);
        }

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
