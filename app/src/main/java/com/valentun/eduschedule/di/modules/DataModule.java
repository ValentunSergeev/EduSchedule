package com.valentun.eduschedule.di.modules;

import android.content.Context;

import com.valentun.eduschedule.data.IRepository;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.data.persistance.PreferenceManager;
import com.valentun.parser.Parser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    @Singleton
    PreferenceManager preferenceManager(Context context) {
        return new PreferenceManager(context);
    }

    @Provides
    @Singleton
    Parser providesParser() {
        return new Parser();
    }

    @Provides
    @Singleton
    IRepository provideRepository() {
        return new Repository();
    }
}
