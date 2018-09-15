package com.valentun.eduschedule.jobs.schedule_checker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.valentun.eduschedule.BuildConfig;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.MyApplication;
import com.valentun.eduschedule.R;
import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class ScheduleCheckerService extends JobService {
    private static final int SESSION_ID = 107;

    @Inject
    IRepository repository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        Disposable disposable = repository.checkScheduleChangedAndUpdate()
                .subscribe(isScheduleChanged -> {
                    if (isScheduleChanged) {
                        showNotification();
                    }
                    jobFinished(job, false);
                }, error ->
                        jobFinished(job, true));

        compositeDisposable.add(disposable);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        compositeDisposable.dispose();
        return false;
    }

    private void initDagger() {
        AppComponent component = MyApplication.INSTANCE.getAppComponent();

        if (component != null) {
            component.inject(this);
        }
    }

    private void showNotification() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(SplashActivity.EXTRA_FORCE_UPDATE, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String title = getString(R.string.schedule_changed_title);
        String message = getString(R.string.schedule_changed_message);

        Notification notification = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(Constants.VIBRATION_PATTERN)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(SESSION_ID, notification);
        }
    }
}
