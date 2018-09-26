package com.valentun.eduschedule.di;

import com.valentun.eduschedule.data.Repository;
import com.valentun.eduschedule.di.modules.AppModule;
import com.valentun.eduschedule.di.modules.DataModule;
import com.valentun.eduschedule.di.modules.JobModule;
import com.valentun.eduschedule.di.modules.NavigationModule;
import com.valentun.eduschedule.di.modules.NetworkModule;
import com.valentun.eduschedule.jobs.schedule_checker.ScheduleCheckerService;
import com.valentun.eduschedule.ui.screens.detail.DetailActivity;
import com.valentun.eduschedule.ui.screens.detail.detail_group.DayGroupPresenter;
import com.valentun.eduschedule.ui.screens.detail.detail_teacher.DayTeacherPresenter;
import com.valentun.eduschedule.ui.screens.main.MainActivity;
import com.valentun.eduschedule.ui.screens.main.MainPresenter;
import com.valentun.eduschedule.ui.screens.main.choose_group.ChooseGroupPresenter;
import com.valentun.eduschedule.ui.screens.main.choose_teacher.ChooseTeacherPresenter;
import com.valentun.eduschedule.ui.screens.main.choose_type.ChooseTypePresenter;
import com.valentun.eduschedule.ui.screens.main.groups.GroupsPresenter;
import com.valentun.eduschedule.ui.screens.main.info.InfoPresenter;
import com.valentun.eduschedule.ui.screens.main.my_schedule.MySchedulePresenter;
import com.valentun.eduschedule.ui.screens.main.settings.SettingsFragment;
import com.valentun.eduschedule.ui.screens.main.teachers.TeachersPresenter;
import com.valentun.eduschedule.ui.screens.school_selector.SchoolSelectActivity;
import com.valentun.eduschedule.ui.screens.school_selector.SchoolSelectPresenter;
import com.valentun.eduschedule.ui.screens.splash.SplashActivity;
import com.valentun.eduschedule.ui.screens.splash.SplashPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        NavigationModule.class,
        NetworkModule.class,
        DataModule.class,
        JobModule.class
})
public interface AppComponent {
    void inject(MainPresenter mainPresenter);
    void inject(MainActivity activity);

    void inject(Repository repository);

    void inject(GroupsPresenter groupsPresenter);

    void inject(TeachersPresenter teachersPresenter);

    void inject(DayGroupPresenter dayGroupPresenter);

    void inject(DayTeacherPresenter dayTeacherPresenter);

    void inject(MySchedulePresenter presenter);
    void inject(SplashActivity activity);

    void inject(SplashPresenter splashPresenter);
    void inject(SchoolSelectPresenter schoolSelectPresenter);

    void inject(SchoolSelectActivity schoolSelectActivity);

    void inject(ChooseGroupPresenter chooseGroupPresenter);

    void inject(ScheduleCheckerService scheduleCheckerService);

    void inject(DetailActivity detailActivity);

    void inject(ChooseTypePresenter chooseTypePresenter);

    void inject(ChooseTeacherPresenter chooseTeacherPresenter);

    void inject(SettingsFragment settingsFragment);

    void inject(InfoPresenter infoPresenter);
}
