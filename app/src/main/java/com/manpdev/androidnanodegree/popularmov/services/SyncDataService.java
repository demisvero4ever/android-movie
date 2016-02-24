package com.manpdev.androidnanodegree.popularmov.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.manpdev.androidnanodegree.popularmov.movie.tasks.SyncMovieTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class SyncDataService extends IntentService {
    private static final String ACTION_SYNC_DATA = "com.manpdev.androidnanodegree.popularmov.services.action.syncdata";

    private static final String EXTRA_JOD_ID = "com.manpdev.androidnanodegree.popularmov.services.extra.jobid";
    public static final String EXTRA_LISTENER_ID = "com.manpdev.androidnanodegree.popularmov.services.extra.listenerid";

    public static final String ACTION_SYNC_COMPLETED = "com.manpdev.androidnanodegree.popularmov.services.action.sync_completed";

    public SyncDataService() {
        super("SyncDataService");
    }

    public static void startSyncData(Context context, int jobId, int listenerId) {
        Intent intent = new Intent(context, SyncDataService.class);
        intent.setAction(ACTION_SYNC_DATA);
        intent.putExtra(EXTRA_JOD_ID, jobId);
        intent.putExtra(EXTRA_LISTENER_ID, listenerId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if(!ACTION_SYNC_DATA.equals(intent.getAction()))
                return;

            switch (intent.getIntExtra(EXTRA_JOD_ID, 0)){
                case SyncMovieTask.TASK_ID:
                    syncMovieData();
                break;
            }

            if(intent.getIntExtra(EXTRA_LISTENER_ID, 0) != 0)
                notifyCaller(intent.getIntExtra(EXTRA_LISTENER_ID, 0));
        }
    }

    private void notifyCaller(int listenerId) {
        Intent broadCastIntent = new Intent(ACTION_SYNC_COMPLETED);
        broadCastIntent.putExtra(EXTRA_LISTENER_ID, listenerId);
        sendBroadcast(broadCastIntent);
    }

    private void syncMovieData() {
        new SyncMovieTask(getApplicationContext()).syncData();
    }
}
