package com.ladyboomerang.pomodoro.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladyboomerang.pomodoro.PomodoroApplication;
import com.ladyboomerang.pomodoro.adapters.HistoryRecyclerViewAdapter;
import com.ladyboomerang.pomodoro.data.PomodoroService;
import com.ladyboomerang.pomodoro.R;
import com.ladyboomerang.pomodoro.model.Pomodoro;
import com.ladyboomerang.pomodoro.ui.DividerItemDecoration;

import javax.inject.Inject;

public class HistoryFragment extends Fragment implements PomodoroService.IPomodoroServiceListener
{
    @Inject PomodoroService service;
    private HistoryRecyclerViewAdapter adapter;

    public HistoryFragment()
    {
    }

    public static HistoryFragment newInstance()
    {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        ((PomodoroApplication) getActivity().getApplication()).component().inject(this);

        service.addPomodoroServiceListener(this);
        adapter = new HistoryRecyclerViewAdapter(service.getHistory());

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HistoryItemDecoration(getResources().getDrawable(R.drawable.divider)));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onPomodoroComplete(Pomodoro pomodoro)
    {
        adapter.add(pomodoro);
    }

    public class HistoryItemDecoration extends DividerItemDecoration
    {

        public HistoryItemDecoration(Drawable divider)
        {
            super(divider);
        }

        @Override
        protected boolean isDecorated(int childIndex, RecyclerView parent)
        {
            RecyclerView.ViewHolder vh = parent.getChildViewHolder(parent.getChildAt(childIndex));
            if (vh instanceof HistoryRecyclerViewAdapter.HeaderViewHolder)
            {
                return false;
            }
            else
            {
                if (childIndex < parent.getChildCount() - 1)
                {
                    vh = parent.getChildViewHolder(parent.getChildAt(childIndex + 1));
                    return !(vh instanceof HistoryRecyclerViewAdapter.HeaderViewHolder);
                }

                return true;
            }
        }
    }
}
