package com.manpdev.androidnanodegree.popularmov.movie.movielist;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manpdev.androidnanodegree.popularmov.R;

public class MovieListFragment extends Fragment implements MovieListContract.PopularMovieListView{

    private MovieSelectionListener mSelectionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movie_list, container, false);
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

    public interface MovieSelectionListener {
        void onSelectMovie(int id);
    }
}
