package com.ladyboomerang.pomodoro.model;

import android.support.annotation.IntDef;

import com.orm.SugarRecord;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

public class Pomodoro extends SugarRecord
{
    public static final int POMODORO_STATUS_STARTED = 0;
    public static final int POMODORO_STATUS_COMPLETED = 1;
    public static final int POMODORO_STATUS_CANCELED = 2;

    public Date start;
    public Date end;
    public @PomodoroStatus int status;

    public Pomodoro()
    {
    }

    public Pomodoro(Date start, @PomodoroStatus int status)
    {
        this.start = start;
        this.status = status;
    }

    @Retention(RetentionPolicy.CLASS)
    @IntDef({POMODORO_STATUS_STARTED, POMODORO_STATUS_COMPLETED, POMODORO_STATUS_CANCELED})
    public @interface PomodoroStatus
    {
    }
}
