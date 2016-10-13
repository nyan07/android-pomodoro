package com.ladyboomerang.pomodoro.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ladyboomerang.pomodoro.PomodoroApplication;
import com.ladyboomerang.pomodoro.R;
import com.ladyboomerang.pomodoro.fragments.HistoryFragment;
import com.ladyboomerang.pomodoro.fragments.PomodoroFragment;
import com.ladyboomerang.pomodoro.model.Pomodoro;
import com.ladyboomerang.pomodoro.data.PomodoroService;
import com.ladyboomerang.pomodoro.adapters.TabsPagerAdapter;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        PomodoroService.IPomodoroServiceListener
{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private SparseArray<Fragment> fragments;
    @Inject PomodoroService service;

    public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Fragment current = fragments.get(viewPager.getCurrentItem());
                if (current instanceof IFloatingActionFragment)
                {
                    ((IFloatingActionFragment) current).onFloatActionButtonClick((FloatingActionButton) view);
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        ((PomodoroApplication) getApplication()).component().inject(this);

        service.addPomodoroServiceListener(this);

        setupViewPager();
        setupTabLayout(viewPager);

        Pomodoro.deleteAll(Pomodoro.class, "status = ?", new String[]{"\"" +Pomodoro.POMODORO_STATUS_STARTED + "\""});
    }

    @Override
    protected void onDestroy()
    {
        service.removePomodoroServiceListener(this);
        super.onDestroy();
    }

    private void setupViewPager()
    {
        String[] tabTitles = getResources().getStringArray(R.array.main_tabs);

        fragments = new SparseArray<>(2);
        fragments.put(0, PomodoroFragment.newInstance());
        fragments.put(1, HistoryFragment.newInstance());

        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager(), tabTitles, fragments);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    private void setupTabLayout(ViewPager viewPager)
    {
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPomodoroComplete(Pomodoro pomodoro)
    {
        fab.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
    }

    @Override
    public void onPageSelected(int position)
    {
        switch(viewPager.getCurrentItem())
        {
            case 0:
                fab.show();
                break;
            case 1:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
    }

    public interface IFloatingActionFragment
    {
        void onFloatActionButtonClick(FloatingActionButton fab);
    }

}