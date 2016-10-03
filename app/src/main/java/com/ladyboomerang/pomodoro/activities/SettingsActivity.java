package com.ladyboomerang.pomodoro.activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ladyboomerang.pomodoro.R;
import com.ladyboomerang.pomodoro.data.PreferenceService;

import java.util.Map;

public class SettingsActivity extends AppCompatPreferenceActivity
{
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener()
            {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value)
                {
                    PreferenceService.getInstance(preference.getContext())
                            .setPreference(preference.getKey(), value);

                    String stringValue = value.toString();

                    if (preference instanceof ListPreference)
                    {
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

                    }
                    else if (preference instanceof RingtonePreference)
                    {
                        if (TextUtils.isEmpty(stringValue))
                        {
                            preference.setSummary(R.string.pref_ringtone_silent);
                        }
                        else
                        {
                            Ringtone ringtone = RingtoneManager.getRingtone(
                                    preference.getContext(), Uri.parse(stringValue));

                            if (ringtone == null)
                            {
                                preference.setSummary(null);
                            }
                            else
                            {
                                String name = ringtone.getTitle(preference.getContext());
                                preference.setSummary(name);
                            }
                        }
                    }
                    else
                    {
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };

    private static void bindPreferenceSummaryToValue(Preference preference)
    {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        Map<String, ?> values = PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getAll();
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, values.get(preference.getKey()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ViewGroup rootView = (ViewGroup) findViewById(R.id.action_bar_root);

        if (rootView != null)
        {
            View view = getLayoutInflater().inflate(R.layout.settings_toolbar, rootView, false);
            rootView.addView(view, 0);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new GeneralPreferenceFragment()).commit();
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            bindPreferenceSummaryToValue(findPreference(
                    PreferenceService.PREFERENCE_POMODORO_INTERVAL));

            bindPreferenceSummaryToValue(findPreference(
                    PreferenceService.PREFERENCE_NOTIFICATIONS_POMODORO_SOUND));

            bindPreferenceSummaryToValue(findPreference(
                    PreferenceService.PREFERENCE_NOTIFICATIONS_POMODORO_RINGTONE));

            bindPreferenceSummaryToValue(findPreference(
                    PreferenceService.PREFERENCE_NOTIFICATIONS_POMODORO_VIBRATE));

            setHasOptionsMenu(true);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            int id = item.getItemId();
            if (id == android.R.id.home)
            {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }
}
