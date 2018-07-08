package com.andela.mrm.room_availability;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.mrm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountDownTimerFragment extends Fragment {

    private TextView myCountDownTimmer;
    private CountDownTimer countDownTimer;
    TextView timeLeftDisplay;
    /**
     * The On text change listener.
     */
    public IOnTextChangeListener iOnTextChangeListener;

    /**
     * The Count down time.
     */
    static final long COUNT_DOWN_TIME = 10000;

    /**
     * Instantiates a new Count down timer fragment.
     */
    public CountDownTimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_count_down_timer,
                container, false);
        myCountDownTimmer = view.findViewById(R.id.count_down_display);
        timeLeftDisplay = view.findViewById(R.id.text_time_left);


        startTimer(COUNT_DOWN_TIME);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;

        if (activity instanceof IOnTextChangeListener) {
            iOnTextChangeListener = (IOnTextChangeListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " "
                    + "must implement CountDownTimerFragment.IOnTextChangeListener");
        }
    }

    /**
     * Timer.
     *
     * @param startTime the start time
     */
    public void startTimer(long startTime) {
        countDownTimer = new CountDownTimer(startTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minute = (int) millisUntilFinished / 60000;
                int second = (int) millisUntilFinished % 60000 / 1000;
                iOnTextChangeListener.onTimeChange(minute);
                setCountDownTime(minute, second);
            }

            @Override
            public void onFinish() {
                // Intentionally left empty as it has no implementation
                String time = "00:00:00";
                setCountDownTimeText(time);
                timeLeftDisplay.setText("Time Till Next Meeting");
                iOnTextChangeListener.onCountDownComplete();
            }
        }.start();
    }

    /**
     * Stop timer.
     */
    public void stopTimer() {
        countDownTimer.cancel();
    }

    /**
     * Sets count down time.
     *
     * @param minute  the minute
     * @param seconds the seconds
     */
    public void setCountDownTime(int minute, int seconds) {
        String convertedMinute;
        String convertedSeconds;
        if (minute < 10) {
            convertedMinute = "0" + minute;
        } else {
             convertedMinute = Integer.toString(minute);
        }

        if (seconds < 10) {
            convertedSeconds = "0" + seconds;
        } else {
            convertedSeconds = Integer.toString(seconds);
        }
        String time = "00:" + convertedMinute + ":" + convertedSeconds;
        setCountDownTimeText(time);
    }

    /**
     * Sets count down time text.
     *
     * @param time the time
     */
    public void setCountDownTimeText(String time) {
        myCountDownTimmer.setText(time);
    }

    /**
     * Sets time remaining text.
     *
     * @param text the text
     */
    public void setTimeRemainingText(String text) {
        timeLeftDisplay.setText(text);
    }

    /**
     * The interface On text change listener.
     */
    interface IOnTextChangeListener {
        /**
         * On time change.
         *
         * @param minutes the minutes
         */
        void onTimeChange(int minutes);

        /**
         * On count down complete.
         */
        void onCountDownComplete();
    }
}
