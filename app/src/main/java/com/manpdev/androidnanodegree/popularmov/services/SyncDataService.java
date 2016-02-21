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

    public SyncDataService() {
        super("SyncDataService");
    }

    public static void startSyncData(Context context, int jobId) {
        Intent intent = new Intent(context, SyncDataService.class);
        intent.setAction(ACTION_SYNC_DATA);
        intent.putExtra(EXTRA_JOD_ID, jobId);
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
        }
    }

    private void syncMovieData() {
        new SyncMovieTask(getApplicationContext()).syncData();
    }
}
