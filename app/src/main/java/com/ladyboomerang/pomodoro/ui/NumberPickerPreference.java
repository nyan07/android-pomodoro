package com.ladyboomerang.pomodoro.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class NumberPickerPreference extends DialogPreference

{
    @TargetApi(21)
    public NumberPickerPreference(Context context)
    {
        super(context);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
