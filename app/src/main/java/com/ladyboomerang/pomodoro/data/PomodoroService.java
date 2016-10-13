package com.ladyboomerang.pomodoro.data;

import com.ladyboomerang.pomodoro.model.Pomodoro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class PomodoroService
{
    private List<IPomodoroServiceListener> listeners;

    public PomodoroService(){}

    public Pomodoro start()
    {
        Pomodoro pomodoro = new Pomodoro(new Date(), Pomodoro.POMODORO_STATUS_STARTED);
        return pomodoro;
    }

    public void cancel(Pomodoro pomodoro)
    {
        pomodoro.end = new Date();
        pomodoro.status = Pomodoro.POMODORO_STATUS_CANCELED;
        pomodoro.save();

        dispatchCompleteListener(pomodoro);
    }

    public void finish(Pomodoro pomodoro)
    {
        pomodoro.end = new Date();
        pomodoro.status = Pomodoro.POMODORO_STATUS_COMPLETED;
        pomodoro.save();

        dispatchCompleteListener(pomodoro);
    }

    private void dispatchCompleteListener(Pomodoro pomodoro)
    {
        if (listeners != null)
        {
            for (IPomodoroServiceListener listener: listeners)
            {
                listener.onPomodoroComplete(pomodoro);
            }
        }
    }


    public long count()
    {
        return Pomodoro.count(Pomodoro.class);
    }

    public List<Pomodoro> getHistory()
    {
        return Pomodoro.listAll(Pomodoro.class, "start desc");
    }

    public void addPomodoroServiceListener(IPomodoroServiceListener listener)
    {
        if (listeners == null)
        {
            listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }

    public void removePomodoroServiceListener(IPomodoroServiceListener listener)
    {
        if (listeners != null)
        {
            listeners.remove(listener);
        }
    }


    public interface IPomodoroServiceListener
    {
        void onPomodoroComplete(Pomodoro pomodoro);
    }
}
