package com.valentun.eduschedule.di;

import com.valentun.eduschedule.root.MainActivity;
import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.modules.AppModule;
import com.valentun.eduschedule.di.modules.DataModule;
import com.valentun.eduschedule.di.modules.NavigationModule;
import com.valentun.eduschedule.di.modules.NetworkModule;
import com.valentun.eduschedule.ui.screens.groups.GroupsPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NavigationModule.class,
        NetworkModule.class,
        DataModule.class
})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(Repository repository);

    void inject(GroupsPresenter groupsPresenter);
}
