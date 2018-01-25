package com.valentun.eduschedule.jobs;

import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.valentun.eduschedule.BuildConfig;
import com.valentun.eduschedule.Constants;
import com.valentun.eduschedule.Constants.JOBS.JobsDef;
import com.valentun.eduschedule.jobs.schedule_checker.ScheduleCheckerService;

public class JobManager {
    private FirebaseJobDispatcher dispatcher;

    public JobManager(Context context) {
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    public void startJob(@JobsDef String jobName) {
        Job job = dispatcher.newJobBuilder()
                .setService(ScheduleCheckerService.class)
                .setTag(jobName)
                .setTrigger(Trigger.executionWindow(BuildConfig.SCHEDULE_CHECK_PERIOD,
                        BuildConfig.SCHEDULE_CHECK_PERIOD +  Constants.NOTIFICATION_DELAY))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .build();

        dispatcher.schedule(job);
    }

    public void stopJob(@JobsDef String jobName) {
        dispatcher.cancel(jobName);
    }
}
