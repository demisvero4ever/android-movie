package com.manpdev.androidnanodegree.popularmov.common.taskproc;

import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import java.util.UUID;

import rx.Observable;
import rx.Subscriber;

/**
 * novoa on 3/20/16.
 */
public abstract class Task<T> implements Observable.OnSubscribe<T> {

    @Override
    public void call(Subscriber<? super T> subscriber) {
        try {

            if (subscriber == null)
                return;

            subscriber.onNext(execute());
            subscriber.onCompleted();
        } catch (Throwable th) {
            subscriber.onError(th);
        }
    }

    @WorkerThread
    public abstract T execute() throws Throwable;
}
