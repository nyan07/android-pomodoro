package com.ladyboomerang.pomodoro.data;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule
{
    public ServiceModule()
    {}

    @Provides
    @Singleton
    public PomodoroService pomodoro()
    {
        return new PomodoroService();
    }

    @Provides
    @Singleton
    public PreferenceService preference(Application application)
    {
        return new PreferenceService(application);
    }
}
