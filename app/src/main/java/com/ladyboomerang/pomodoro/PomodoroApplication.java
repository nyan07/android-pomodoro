package com.ladyboomerang.pomodoro;

import com.ladyboomerang.pomodoro.data.ServiceModule;
import com.orm.SugarApp;
import com.orm.SugarContext;

public class PomodoroApplication extends SugarApp
{
    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .serviceModule(new ServiceModule())
                .build();


        SugarContext.init(this);
   }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        SugarContext.terminate();
    }

    public ApplicationComponent component()
    {
        return applicationComponent;
    }
}
