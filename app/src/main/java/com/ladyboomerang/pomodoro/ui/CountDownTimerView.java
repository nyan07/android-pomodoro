package com.ladyboomerang.pomodoro.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ladyboomerang.pomodoro.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CountDownTimerView extends RelativeLayout
{
    private TextView label;
    private TextView elapsedTime;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private ICountDownTimerViewListener listener;
    private boolean isPlaying = false;
    private int max;

    private View front;

    public CountDownTimerView(Context context)
    {
        super(context);
        initialize();
    }

    public CountDownTimerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initialize();
    }

    public CountDownTimerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(21)
    public CountDownTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_count_down_timer, this);

        // set camera distance to flip animation
        setCameraDistance(getResources().getDisplayMetrics().density * 8000);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        label = (TextView) findViewById(R.id.label);
        elapsedTime = (TextView) findViewById(R.id.elapsed_time);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        front = findViewById(R.id.front);
   }

    public void setLabel(int resId)
    {
        label.setText(resId);
    }

    public void setMax(int value)
    {
        max = value;
        if (!isPlaying())
        {
            progressBar.setMax(value);
            setElapsedTime(value);
        }
    }

    public void setElapsedTime(long millis)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss", Locale.getDefault());
        elapsedTime.setText(formatter.format(new Date(millis)));
        progressBar.setProgress((int) (progressBar.getMax() - millis));
    }

    public void start()
    {
        if (!isPlaying)
        {
            progressBar.setMax(max);
            timer = new CountDownTimer(progressBar.getMax(), 100)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    setElapsedTime(millisUntilFinished);
                }

                @Override
                public void onFinish()
                {
                    isPlaying = false;

                    if (listener != null)
                    {
                        listener.onTimerFinished();
                    }

                    reset();
                }
            };

            timer.start();
            isPlaying = true;
        }
    }

    public void stop()
    {
        if (isPlaying)
        {
            if (timer != null)
            {
                timer.cancel();
                timer = null;
                isPlaying = false;
            }

            reset();
        }
    }

    public boolean isPlaying()
    {
        return isPlaying;
    }


    private void reset()
    {
        Animator animator = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 0);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
        flip();
    }

    public void setTimerViewListener(ICountDownTimerViewListener listener)
    {
        this.listener = listener;
    }

    private void flip()
    {
        final int duration = 500;
        AnimatorSet animSet = new AnimatorSet();

        Animator anim1 = ObjectAnimator.ofFloat(front, "rotationY", front.getRotationY(), -90);
        anim1.setInterpolator(new LinearInterpolator());
        anim1.setDuration(duration/4);

        Animator anim2 = ObjectAnimator.ofFloat(front, "rotationY", -90, -180);
        anim2.setInterpolator(new LinearInterpolator());
        anim2.setDuration(duration/4);

        Animator anim3 = ObjectAnimator.ofFloat(front, "rotationY",  180, 0);
        anim3.setDuration(duration/2);
        anim3.setInterpolator(new DecelerateInterpolator());

        animSet.play(anim1).before(anim2);
        animSet.play(anim3).after(anim2);

        anim1.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animator)
            {

            }

            @Override
            public void onAnimationEnd(Animator animator)
            {
                setElapsedTime(max);
            }

            @Override
            public void onAnimationCancel(Animator animator)
            {

            }

            @Override
            public void onAnimationRepeat(Animator animator)
            {

            }
        });

        animSet.start();
    }

    public interface ICountDownTimerViewListener
    {
        void onTimerFinished();
    }
}