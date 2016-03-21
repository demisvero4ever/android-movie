package com.manpdev.androidnanodegree.popularmov.common.tasks;

import rx.Subscriber;

/**
 * novoa on 3/20/16.
 */
public abstract class CachedTask<T> extends Task<T>{

    @Override
    public void call(Subscriber<? super T> subscriber) {
        if(subscriber == null)
            return;

        try {
            subscriber.onNext(cachedResult());

            T result = execute();
            cacheResult(result);

            subscriber.onNext(result);
            subscriber.onCompleted();

        }catch (Throwable th){
            subscriber.onError(th);
        }
    }

    public abstract T execute();
    public abstract void cacheResult(T result);
    public abstract T cachedResult();
}
