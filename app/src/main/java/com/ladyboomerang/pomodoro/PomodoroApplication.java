package com.ladyboomerang.pomodoro;

import com.ladyboomerang.pomodoro.model.Pomodoro;
import com.orm.SugarApp;
import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class PomodoroApplication extends SugarApp
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        SugarContext.init(this);
   }

    @Override
    public void onTerminate()
    {
        super.onTerminate();
        SugarContext.terminate();
    }
}
