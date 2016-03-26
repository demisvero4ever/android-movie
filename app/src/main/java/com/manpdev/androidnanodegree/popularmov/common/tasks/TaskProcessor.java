package com.manpdev.androidnanodegree.popularmov.common.tasks;

import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * novoa on 3/20/16.
 */

@SuppressWarnings({"unchecked", "unused"})
public class TaskProcessor {

    private static SimpleArrayMap<String, Object> sResult = new SimpleArrayMap<>();
    private static SimpleArrayMap<String, Callback> sCallbacks = new SimpleArrayMap<>();
    private static SimpleArrayMap<String, Subscription> sSubscriptions = new SimpleArrayMap<>();

    private static int CORES = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService sEXECUTOR = Executors.newFixedThreadPool(CORES);

    public void cleanOperation(String operationId) {
        sResult.remove(operationId);
        sCallbacks.remove(operationId);
        sSubscriptions.remove(operationId);
    }

    public void perform(String operationId, Task task) {
        perform(operationId, task, null);
    }

    public void perform(String operationId, Task task, @Nullable Callback callback) {
        Observable operation = buildOperation(task);

        if (callback != null) {
            sCallbacks.remove(operationId);
            sCallbacks.put(operationId, callback);
        }

        sResult.remove(operationId);
        sSubscriptions.put(operationId, operation.subscribe(new ProcessorObserver(operationId)));
    }

    public <T> T getPreviousResult(String operationId, Class<T> clazz) {
        try {

            if (sResult.containsKey(operationId))
                return clazz.cast(sResult.get(operationId));

        } catch (Exception ex) {
            return null;
        }

        return null;
    }

    public void unSubscribeOperation(String operationId) {
        sCallbacks.remove(operationId);
    }

    private Observable buildOperation(Task task) {
        return Observable.create(task)
                .subscribeOn(Schedulers.from(sEXECUTOR))
                .observeOn(Schedulers.from(sEXECUTOR));
    }

    private class ProcessorObserver implements Observer<Object> {
        private static final String TAG = "ProcessorObserver";
        
        private String mOperationId;

        public ProcessorObserver(String operationId) {
            this.mOperationId = operationId;
        }

        @Override
        public void onCompleted() {
            sSubscriptions.remove(mOperationId);
        }

        @Override
        public void onError(final Throwable e) {
            sSubscriptions.remove(mOperationId);

            if (sCallbacks.containsKey(mOperationId)) {

                Callback callback = sCallbacks.get(mOperationId);

                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onError(e);
                    }
                })
                        .subscribeOn(Schedulers.from(sEXECUTOR))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(callback);
            }
        }

        @Override
        public void onNext(final Object result) {
            Log.d(TAG, "execute() called from: " + Thread.currentThread().getName());
            
            if (!sCallbacks.containsKey(mOperationId))
                sResult.put(mOperationId, result);
            else {
                Callback callback = sCallbacks.get(mOperationId);

                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext(result);
                        subscriber.onCompleted();

                        sCallbacks.remove(mOperationId);
                    }
                })
                        .subscribeOn(Schedulers.immediate())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(callback);
            }
        }
    }
}
