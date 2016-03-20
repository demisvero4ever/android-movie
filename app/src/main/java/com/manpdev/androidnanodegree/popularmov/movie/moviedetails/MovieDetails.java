package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.taskproc.Callback;
import com.manpdev.androidnanodegree.popularmov.common.taskproc.TaskProcessor;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieReviewWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieTrailerWrapperModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.FavoriteMovieCheckOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.MarkMovieAsFavoriteOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.RemoveMovieFromFavoritesOperation;

/**
 * android-nd-popular-mov android-nd-popular-mov novoa on 3/19/16.
 */
public class MovieDetails implements MovieDetailsContract.MovieDetailsPresenter {

    private static final String TAG = "MovieDetails";

    private static final String CHECK_FAV_TASK_ID = "123";
    private static final String GET_TRAILERS_TASK_ID = "345";
    private static final String GET_REVIEW_TASK_ID = "567";
    private static final String MARK_AS_FAV_TASK_ID = "890";
    private static final String REM_FROM_FAV_TASK_ID = "101";

    private Context mContext;

    private TaskProcessor mTaskProcessor;

    private MovieDetailsContract.MovieDetailsView mView;

    public MovieDetails(Context mContext, MovieDetailsContract.MovieDetailsView view) {
        this.mContext = mContext;
        this.mTaskProcessor = new TaskProcessor();
        this.mView = view;
    }

    @Override
    public void loadMovieDetails(int cloudMovieId) {
        mTaskProcessor.perform(CHECK_FAV_TASK_ID, new FavoriteMovieCheckOperation(mContext, cloudMovieId), mFavCheckCallback);
    }

    @Override
    public void saveMovieAsFavorite(MovieModel movie) {
        mTaskProcessor.perform(MARK_AS_FAV_TASK_ID, new MarkMovieAsFavoriteOperation(mContext, movie), mFavAddedCallback);
    }

    @Override
    public void removeMovieFromFavorites(int movieId) {
        mTaskProcessor.perform(REM_FROM_FAV_TASK_ID, new RemoveMovieFromFavoritesOperation(mContext, movieId), mFavRemovedCallback);
    }

    //Callbacks
    private Callback<Boolean> mFavCheckCallback = new Callback<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            mView.favoriteSelection(result);
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

    private Callback<Boolean> mFavAddedCallback = new Callback<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            //show snackbar
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

    private Callback<Boolean> mFavRemovedCallback = new Callback<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            //show snackbar
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

    private Callback<MovieTrailerWrapperModel> mGetTrailerListCallback = new Callback<MovieTrailerWrapperModel>() {
        @Override
        public void onResult(MovieTrailerWrapperModel result) {

        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

    private Callback<MovieReviewWrapperModel> mGetReviewListCallback = new Callback<MovieReviewWrapperModel>() {
        @Override
        public void onResult(MovieReviewWrapperModel result) {

        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };
}
