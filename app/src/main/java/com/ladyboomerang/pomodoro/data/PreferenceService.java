package com.ladyboomerang.pomodoro.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferenceService
{
    public static final String PREFERENCE_POMODORO_INTERVAL = "pomodoro_interval";
    public static final String PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE = "notifications_pomodoro_vibrate";
    public static final String PREFERENCE_NOTIFICATIONS_POMODORO_SOUND = "notifications_pomodoro_sound";
    public static final String PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE = "notifications_pomodoro_ringtone";

    private Map<String, Object> values = new HashMap<>();
    private static PreferenceService instance;
    private List<PreferenceService.IPreferenceServiceListener> listeners;
    private Context context;

    public static PreferenceService getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new PreferenceService(context);
        }
        return instance;
    }

    public PreferenceService(Context context)
    {
        this.context = context;
    }

    public int getPomodoroInterval()
    {
        int value;

        if (values.get(PREFERENCE_POMODORO_INTERVAL) == null)
        {
            value = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                    .getString(PREFERENCE_POMODORO_INTERVAL, "25"));

            values.put(PREFERENCE_POMODORO_INTERVAL, value);
        }
        else
        {
            value = Integer.parseInt(values.get(PREFERENCE_POMODORO_INTERVAL).toString());
        }

        return value * 60 * 1000;
    }

    public void setPreference(String key, Object value)
    {
        values.put(key, value);
        dispatchPreferenceChangeListener(key);
    }

    public boolean isVibrateEnabled()
    {
        boolean value;

        if (values.get(PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE) == null)
        {
            value = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE, false);

            values.put(PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE, value);
        }
        else
        {
            value = (boolean) values.get(PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE);
        }

        return value;
    }

    public boolean isSoundNotificationEnabled()
    {

        boolean value;

        if (values.get(PREFERENCE_NOTIFICATIONS_POMODORO_SOUND) == null)
        {
            value = PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(PREFERENCE_NOTIFICATIONS_POMODORO_SOUND, false);

            values.put(PREFERENCE_NOTIFICATIONS_POMODORO_SOUND, value);
        }
        else
        {
            value = (boolean) values.get(PREFERENCE_NOTIFICATIONS_POMODORO_SOUND);
        }

        return value;
    }

    public Uri getRingtoneUri()
    {
            String value;

            if (values.get(PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE) == null)
            {
                value = PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE, null);

                values.put(PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE, value);
            }
            else
            {
                value = values.get(PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE).toString();
            }

            return Uri.parse(value);
    }

    private void dispatchPreferenceChangeListener(String preferenceKey)
    {
        if (listeners != null)
        {
            for (IPreferenceServiceListener listener: listeners)
            {
                listener.onPreferenceChange(preferenceKey);
            }
        }
    }

    public void addPreferenceServiceListener(IPreferenceServiceListener listener)
    {
        if (listeners == null)
        {
            listeners = new ArrayList<>();
        }
        this.listeners.add(listener);
    }

    public void removePreferenceServiceListener(IPreferenceServiceListener listener)
    {
        if (listeners != null)
        {
            listeners.remove(listener);
        }
    }

    public interface IPreferenceServiceListener
    {
        void onPreferenceChange(String preferenceKey);
    }
}
