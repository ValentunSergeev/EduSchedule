package com.valentun.eduschedule;


import android.app.Application;

import com.valentun.eduschedule.di.AppComponent;
import com.valentun.eduschedule.di.DaggerAppComponent;
import com.valentun.eduschedule.di.modules.AppModule;

public class MyApplication extends Application {
    public static MyApplication INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public AppComponent getAppComponent() {
        if(appComponent == null)
            initAppComponent();

        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();
    }
}
