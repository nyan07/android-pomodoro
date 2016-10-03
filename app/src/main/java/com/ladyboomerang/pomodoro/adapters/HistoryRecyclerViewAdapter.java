package com.ladyboomerang.pomodoro.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.ladyboomerang.pomodoro.R;
import com.ladyboomerang.pomodoro.model.Pomodoro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryRecyclerViewAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder>
{
    private class Section
    {
        CharSequence title;
        List<Pomodoro> items;
    }

    private final List<Section> sections;

    @Override
    public int getSectionCount()
    {
        return sections.size();
    }

    @Override
    public int getItemCount(int section) {
        return sections.get(section).items.size();
    }

    public void add(Pomodoro pomodoro)
    {
        Section section = retrieveSectionToAddItem(sections.size() > 0 ? sections.get(0) : null, pomodoro);
        section.items.add(0, pomodoro);

        notifyDataSetChanged();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder vh, int section)
    {
        HeaderViewHolder holder = (HeaderViewHolder) vh;
        holder.label.setText(sections.get(section).title);
    }

    public HistoryRecyclerViewAdapter(List<Pomodoro> items)
    {
        sections = new ArrayList<Section>();
        Section section = null;

        for (int i = 0; i < items.size(); i++)
        {
            Pomodoro item = items.get(i);
            if (i > 0) section = sections.get(sections.size() - 1);

            section = retrieveSectionToAddItem(section, item);
            section.items.add(item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_HEADER)
        {
            return new HeaderViewHolder(inflater.inflate(R.layout.fragment_history_list_section, parent, false));
        }
        else
        {
            return new ItemViewHolder(inflater.inflate(R.layout.fragment_history_list_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int section, int relativePosition, int absolutePosition) {

        ItemViewHolder holder = (ItemViewHolder) vh;

        Pomodoro item = sections.get(section).items.get(relativePosition);
        long startInMillis = item.start.getTime();

        if (DateUtils.isToday(startInMillis))
        {
            holder.startedAt.setText(DateUtils.getRelativeTimeSpanString(startInMillis,
                    System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_ALL));
        }
        else
        {
            holder.startedAt.setText(DateUtils.formatDateTime(holder.itemView.getContext(),
                    startInMillis, DateUtils.FORMAT_SHOW_TIME));
        }

        if (item.end != null)
        {
            long diffMillis = item.end.getTime() - item.start.getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
            holder.totalTime.setText(formatter.format(new Date(diffMillis)));
        }

        int statusResId = item.status == Pomodoro.POMODORO_STATUS_COMPLETED ?
                R.drawable.ic_alarm_on_black_24dp : R.drawable.ic_alarm_off_black_24dp;
        holder.status.setImageResource(statusResId);
    }

    private Section retrieveSectionToAddItem(Section lastSection, Pomodoro pomodoro)
    {
        CharSequence itemPrettyDate = DateUtils.getRelativeTimeSpanString(pomodoro.start.getTime(),
                System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS);

        if (lastSection == null || !lastSection.title.equals(itemPrettyDate))
        {
            lastSection = new Section();
            lastSection.title = itemPrettyDate;
            lastSection.items = new ArrayList<>();
            sections.add(lastSection);
        }

        return lastSection;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public TextView label;

        HeaderViewHolder(View view)
        {
            super(view);
            label = (TextView) view;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        public TextView totalTime;
        public TextView startedAt;
        public ImageView status;

        ItemViewHolder(View view)
        {
            super(view);
            totalTime = (TextView) view.findViewById(R.id.total_time);
            startedAt = (TextView) view.findViewById(R.id.started_at);
            status = (ImageView) view.findViewById(R.id.status);
        }
    }
}
