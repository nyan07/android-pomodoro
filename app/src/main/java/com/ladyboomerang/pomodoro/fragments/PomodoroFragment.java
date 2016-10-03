package com.ladyboomerang.pomodoro.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladyboomerang.pomodoro.activities.MainActivity;
import com.ladyboomerang.pomodoro.R;
import com.ladyboomerang.pomodoro.data.PreferenceService;
import com.ladyboomerang.pomodoro.model.Pomodoro;
import com.ladyboomerang.pomodoro.data.PomodoroService;
import com.ladyboomerang.pomodoro.ui.CountDownTimerView;
import com.ladyboomerang.pomodoro.utils.NotificationUtils;

public class PomodoroFragment extends Fragment implements MainActivity.IFloatingActionFragment,
        CountDownTimerView.ICountDownTimerViewListener, PreferenceService.IPreferenceServiceListener
{
    private Pomodoro pomodoro;
    private PomodoroService service;
    private CountDownTimerView timerView;
    private PreferenceService preferences;

    public PomodoroFragment()
    {
        service = PomodoroService.getInstance();
    }

    public static PomodoroFragment newInstance()
    {
        return new PomodoroFragment();
    }

    @Override
    public void onFloatActionButtonClick(FloatingActionButton fab)
    {
        if (timerView.isPlaying())
        {
            fab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
            timerView.stop();
            service.cancel(pomodoro);
        }
        else
        {
            fab.setImageResource(R.drawable.ic_stop_black_24dp);
            pomodoro = service.start();
            timerView.start();
        }
    }

    @Override
    public void onTimerFinished()
    {
        service.finish(pomodoro);

        if (preferences.isVibrateEnabled())
        {
            NotificationUtils.vibrate(getContext());
        }

        if (preferences.isSoundNotificationEnabled())
        {
            NotificationUtils.playRingtone(getContext(), preferences.getRingtoneUri());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        timerView = (CountDownTimerView) view.findViewById(R.id.timer_front);
        timerView.setElapsedTime(preferences.getPomodoroInterval());
        timerView.setMax(preferences.getPomodoroInterval());
        timerView.setTimerViewListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        preferences = PreferenceService.getInstance(context.getApplicationContext());
        preferences.addPreferenceServiceListener(this);
    }

    @Override
    public void onPreferenceChange(String preferenceKey)
    {
        if (preferenceKey.equals(PreferenceService.PREFERENCE_POMODORO_INTERVAL))
        {
            timerView.setMax(preferences.getPomodoroInterval());
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        preferences.removePreferenceServiceListener(this);
        preferences = null;
    }
}
