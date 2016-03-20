package com.manpdev.androidnanodegree.popularmov.common.taskproc;

/**
 * novoa on 3/20/16.
 */
public abstract class Callback<T> implements rx.Observer<T>{

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onNext(T t) {
        onResult(t);
    }

    public abstract void onResult(T result);
    public abstract void onFailure(Throwable th);
}
