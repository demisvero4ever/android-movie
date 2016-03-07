package com.manpdev.androidnanodegree.popularmov.movie.data.operation;

import android.content.Context;

import com.manpdev.androidnanodegree.popularmov.movie.Preferences;
import com.manpdev.androidnanodegree.popularmov.movie.data.api.MovieApiRequester;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.base.Operation;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public class GetMovieListOperation extends Operation<MovieWrapperModel> {

    private String mSelectionOption;
    private MovieApiRequester mRequester;

    public GetMovieListOperation(Context mContext) {
        this.mRequester = MovieApiRequester.getInstance(mContext);

        this.mSelectionOption = Preferences.BY_POPULARITY_DESC;
    }

    public GetMovieListOperation setSelectionOption(@Preferences.SelectionOptions String selectionOption){
        this.mSelectionOption = selectionOption;
        return this;
    }

    public void execute(){
        mRequester.getMovieApi()
                .getSortedMovieList(mRequester.getMovieApiKey(), mSelectionOption)
                .enqueue(getOperationCallBack());
    }
}
