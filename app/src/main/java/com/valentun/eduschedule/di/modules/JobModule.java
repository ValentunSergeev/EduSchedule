package com.valentun.eduschedule.di.modules;

import android.content.Context;

import com.valentun.eduschedule.jobs.JobManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JobModule {

    @Provides
    @Singleton
    public JobManager provideJobManager(Context context) {
        return new JobManager(context);
    }
}
