package com.manpdev.androidnanodegree.popularmov.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.manpdev.androidnanodegree.popularmov.movie.tasks.SyncMovieTask;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class SyncDataService extends IntentService {
    private static final String ACTION_SYNC_DATA = "com.manpdev.androidnanodegree.popularmov.services.action.syncdata";

    public static final String EXTRA_JOD_ID = "com.manpdev.androidnanodegree.popularmov.services.extra.jobid";
    private static final String EXTRA_NOTIFY = "com.manpdev.androidnanodegree.popularmov.services.extra.notify";

    public static final String ACTION_SYNC_COMPLETED = "com.manpdev.androidnanodegree.popularmov.services.action.sync_completed";
    public static final String ACTION_SYNC_FAILED = "com.manpdev.androidnanodegree.popularmov.services.action.sync_failed";

    public SyncDataService() {
        super("SyncDataService");
    }

    public static void startSyncData(Context context, int jobId, boolean notify) {
        Intent intent = new Intent(context, SyncDataService.class);
        intent.setAction(ACTION_SYNC_DATA);
        intent.putExtra(EXTRA_JOD_ID, jobId);
        intent.putExtra(EXTRA_NOTIFY, notify);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (!ACTION_SYNC_DATA.equals(intent.getAction()))
                return;

            boolean syncResult = false;

            switch (intent.getIntExtra(EXTRA_JOD_ID, 0)) {
                case SyncMovieTask.TASK_ID:
                    syncResult = syncMovieData();
                    break;
            }

            if (intent.getBooleanExtra(EXTRA_NOTIFY, false))
                notifyCaller(syncResult, intent.getIntExtra(EXTRA_JOD_ID, 0));
        }
    }

    private void notifyCaller(boolean success, int jobId) {
        Intent broadCastIntent = success ? new Intent(ACTION_SYNC_COMPLETED) :
                new Intent(ACTION_SYNC_FAILED);

        broadCastIntent.putExtra(EXTRA_JOD_ID, jobId);
        sendBroadcast(broadCastIntent);
    }

    private boolean syncMovieData() {
        return new SyncMovieTask(getApplicationContext()).syncData();
    }
}
