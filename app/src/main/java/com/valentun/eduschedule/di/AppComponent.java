package com.valentun.eduschedule.di;

import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.modules.AppModule;
import com.valentun.eduschedule.di.modules.DataModule;
import com.valentun.eduschedule.di.modules.NavigationModule;
import com.valentun.eduschedule.di.modules.NetworkModule;
import com.valentun.eduschedule.ui.screens.detail_group.DayGroupPresenter;
import com.valentun.eduschedule.ui.screens.detail_teacher.DayTeacherPresenter;
import com.valentun.eduschedule.ui.screens.main.MainActivity;
import com.valentun.eduschedule.ui.screens.main.groups.GroupsPresenter;
import com.valentun.eduschedule.ui.screens.main.my_schedule.MySchedulePresenter;
import com.valentun.eduschedule.ui.screens.main.teachers.TeachersPresenter;

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

    void inject(TeachersPresenter teachersPresenter);

    void inject(DayGroupPresenter dayGroupPresenter);

    void inject(DayTeacherPresenter dayTeacherPresenter);

    void inject(MySchedulePresenter presenter);
}
