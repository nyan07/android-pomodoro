package com.ladyboomerang.pomodoro.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class NotificationUtils
{
    public static void vibrate(Context context)
    {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public static void playRingtone(Context context, Uri uri)
    {
        Ringtone r = RingtoneManager.getRingtone(context, uri);
        r.play();
    }
}
