package com.manpdev.androidnanodegree.popularmov.movie.data.operation.base;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by novoa.pro@gmail.com on 3/4/16
 */
public abstract class Operation<T> {

    protected Observer<T> mObserver;

    public Operation<T> subscribe(Observer<T> observer) {
        this.mObserver = observer;
        return this;
    }

    public void unsubscribe(){
        this.mObserver = null;
    }

    protected Callback<T> getOperationCallBack(){
        return new Callback<T>() {
            @Override
            public void onResponse(Response<T> response, Retrofit retrofit) {
                if(mObserver == null)
                    return;

                if(response.isSuccess())
                    mObserver.onResult(response.body());
                else
                    mObserver.onError(new Throwable(response.errorBody().toString()));
            }

            @Override
            public void onFailure(Throwable t) {
                if(mObserver == null)
                    return;

                mObserver.onError(t);
            }
        };
    }
}
