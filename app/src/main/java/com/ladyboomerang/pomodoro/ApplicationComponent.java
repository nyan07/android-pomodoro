package com.ladyboomerang.pomodoro;

import android.app.Application;

import com.ladyboomerang.pomodoro.activities.MainActivity;
import com.ladyboomerang.pomodoro.data.ServiceModule;
import com.ladyboomerang.pomodoro.fragments.HistoryFragment;
import com.ladyboomerang.pomodoro.fragments.PomodoroFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { ApplicationModule.class, ServiceModule.class })
public interface ApplicationComponent
{
    void inject(PomodoroApplication application);
    void inject(MainActivity mainActivity);
    void inject(HistoryFragment historyFragment);
    void inject(PomodoroFragment pomodoroFragment);

    Application application();
}
