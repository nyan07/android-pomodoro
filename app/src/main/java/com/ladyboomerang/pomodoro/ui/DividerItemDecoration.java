package com.ladyboomerang.pomodoro.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration
{
    private Drawable drawable;
    private int orientation;

    public DividerItemDecoration(Drawable divider)
    {
        drawable = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state)
    {
        if (orientation == LinearLayoutManager.HORIZONTAL)
        {
            drawHorizontalDividers(canvas, parent);
        }
        else if (orientation == LinearLayoutManager.VERTICAL)
        {
            drawVerticalDividers(canvas, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0)
        {
            return;
        }

        orientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
        if (orientation == LinearLayoutManager.HORIZONTAL)
        {
            outRect.left = drawable.getIntrinsicWidth();
        }
        else if (orientation == LinearLayoutManager.VERTICAL)
        {
            outRect.top = drawable.getIntrinsicHeight();
        }
    }

    private void drawHorizontalDividers(Canvas canvas, RecyclerView parent)
    {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++)
        {
            if (isDecorated(i, parent))
            {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentLeft = child.getRight() + params.rightMargin;
                int parentRight = parentLeft + drawable.getIntrinsicWidth();

                drawable.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                drawable.draw(canvas);
            }
        }
    }

    private void drawVerticalDividers(Canvas canvas, RecyclerView parent)
    {
        int parentLeft = parent.getPaddingLeft();
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++)
        {
            if (isDecorated(i, parent))
            {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentTop = child.getBottom() + params.bottomMargin;
                int parentBottom = parentTop + drawable.getIntrinsicHeight();

                drawable.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                drawable.draw(canvas);
            }
        }
    }

    protected boolean isDecorated(int childIndex, RecyclerView parent)
    {
        return true;
    }
}