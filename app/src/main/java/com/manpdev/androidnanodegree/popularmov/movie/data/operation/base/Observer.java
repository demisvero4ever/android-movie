package com.manpdev.androidnanodegree.popularmov.movie.data.operation.base;

/**
 * Created by novoa.pro@gmail.com on 3/5/16
 */
public interface Observer<T> {
    void onResult(T data);
    void onError(Throwable th);
}
