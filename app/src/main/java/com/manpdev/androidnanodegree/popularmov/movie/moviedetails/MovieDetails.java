package com.manpdev.androidnanodegree.popularmov.movie.moviedetails;

import android.content.Context;
import android.util.Log;

import com.manpdev.androidnanodegree.popularmov.common.tasks.Callback;
import com.manpdev.androidnanodegree.popularmov.common.tasks.TaskProcessor;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.MovieModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.model.wrapper.MovieExtrasModel;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.FavoriteMovieCheckOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.MarkMovieAsFavoriteOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.local.RemoveMovieFromFavoritesOperation;
import com.manpdev.androidnanodegree.popularmov.movie.data.operation.net.GetMovieExtrasOperation;

/**
 * android-nd-popular-mov android-nd-popular-mov novoa on 3/19/16.
 */
public class MovieDetails implements MovieDetailsContract.MovieDetailsPresenter {

    private static final String TAG = "MovieDetails";

    private static final String CHECK_FAV_TASK_ID = "123";
    private static final String GET_EXTRAS_TASK_ID = "567";
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
        mTaskProcessor.perform(GET_EXTRAS_TASK_ID, new GetMovieExtrasOperation(mContext, cloudMovieId), mGetMovieExtrasCallback);
    }

    @Override
    public void saveMovieAsFavorite(MovieModel movie) {
        mTaskProcessor.perform(MARK_AS_FAV_TASK_ID, new MarkMovieAsFavoriteOperation(mContext, movie), mSaveAsFavCallback);
    }

    @Override
    public void removeMovieFromFavorites(int movieId) {
        mTaskProcessor.perform(REM_FROM_FAV_TASK_ID, new RemoveMovieFromFavoritesOperation(mContext, movieId), mRemAsFavCallback);
    }

    @Override
    public void removeCallBacks() {
        mTaskProcessor.unSubscribeOperation(CHECK_FAV_TASK_ID);
        mTaskProcessor.unSubscribeOperation(MARK_AS_FAV_TASK_ID);
        mTaskProcessor.unSubscribeOperation(REM_FROM_FAV_TASK_ID);
        mTaskProcessor.unSubscribeOperation(GET_EXTRAS_TASK_ID);
    }

    //Callbacks
    private Callback<Boolean> mSaveAsFavCallback = new Callback<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            mView.favoriteSelection(result);
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

    private Callback<Boolean> mRemAsFavCallback = new Callback<Boolean>() {
        @Override
        public void onResult(Boolean result) {
            mView.favoriteSelection(!result);
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };

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

    private Callback<MovieExtrasModel> mGetMovieExtrasCallback = new Callback<MovieExtrasModel>() {
        @Override
        public void onResult(MovieExtrasModel result) {
            mView.showMovieExtras(result);
            if(result.getTrailers().size() > 0)
                mView.enableTrailerSharing(result.getTrailers().get(0).getKey());
            else
                mView.disableTrailerSharing();
        }

        @Override
        public void onFailure(Throwable th) {
            Log.e(TAG, "onFailure: ", th);
        }
    };
}
