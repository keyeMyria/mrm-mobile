package com.andela.mrm.room_booking.room_availability.views;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.mrm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingRoomDetailFragment extends Fragment {

    private TextView minuteDisplayTextView;
    private Button checkInEarlyButton;
    private Button meetingRoomAvailabilityDisplay;
    private Button meetingRoomAvailabilityInUse;
    private Button endMeetingButton;
    private Button checkInButton;
    private TextView meetingRoomTextTitle;
    private LinearLayout extendTime;
    private LinearLayout bookFreeTime;
    private IOnStartCountDown iOnStartCountDown;

    /**
     * Instantiates a new Meeting room detail fragment.
     */
    public MeetingRoomDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_room_detail,
                container, false);
        minuteDisplayTextView = view.findViewById(R.id.btn_room_availability_time_text);
        checkInEarlyButton = view.findViewById(R.id.btn_check_in_early);
        endMeetingButton = view.findViewById(R.id.btn_end_meeting);
        checkInButton = view.findViewById(R.id.btn_check_in);
        meetingRoomTextTitle = view.findViewById(R.id.text_meeting_title);
        extendTime = view.findViewById(R.id.layout_extra_time);
        bookFreeTime = view.findViewById(R.id.layout_free_extra_time);
        meetingRoomAvailabilityDisplay = view.findViewById(R.id.btn_room_availability_time_text);
        meetingRoomAvailabilityInUse =
                view.findViewById(R.id.btn_room_availability_in_use_display);
        checkInEarlyButton.setOnClickListener(v -> startMeeting());
        endMeetingButton.setOnClickListener(v -> endMeeting());
        checkInButton.setOnClickListener(v -> startMeeting());
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        if (activity instanceof MeetingRoomDetailFragment.IOnStartCountDown) {
            iOnStartCountDown = (IOnStartCountDown) activity;
        } else {
            throw new ClassCastException("Must "
                    + "implement MeetingRoomDetailFragment.IOnStartCountDown");
        }
    }

    /**
     * Update minute.
     *
     * @param minute the minute
     */
    public void updateMinute(int minute) {
        String text = "Free for " + Integer.toString(minute) + " minutes";
        minuteDisplayTextView.setText(text);
    }

    /**
     * Start meeting.
     */
    public void startMeeting() {
        checkInEarlyButton.setVisibility(View.GONE);
        meetingRoomAvailabilityDisplay.setVisibility(View.GONE);
        endMeetingButton.setVisibility(View.VISIBLE);
        checkInButton.setVisibility(View.GONE);
        meetingRoomTextTitle.setVisibility(View.VISIBLE);
        meetingRoomAvailabilityInUse.setVisibility(View.VISIBLE);
        extendTime.setVisibility(View.VISIBLE);
        bookFreeTime.setVisibility(View.GONE);
        iOnStartCountDown.startCountDown(9000);
    }

    /**
     * End meeting.
     */
    public void endMeeting() {
        endMeetingButton.setVisibility(View.GONE);
        checkInEarlyButton.setVisibility(View.VISIBLE);
        meetingRoomAvailabilityDisplay.setVisibility(View.VISIBLE);
        meetingRoomAvailabilityInUse.setVisibility(View.GONE);
        bookFreeTime.setVisibility(View.GONE);
        extendTime.setVisibility(View.GONE);
        iOnStartCountDown.onMeetingEnded();
    }

    /**
     * Display check in screen.
     */
    public void displayCheckInScreen() {
        // to be implemented
        checkInEarlyButton.setVisibility(View.GONE);
        checkInButton.setVisibility(View.VISIBLE);
        meetingRoomAvailabilityInUse.setVisibility(View.GONE);
        meetingRoomTextTitle.setVisibility(View.GONE);
        endMeetingButton.setVisibility(View.GONE);
        bookFreeTime.setVisibility(View.VISIBLE);
        extendTime.setVisibility(View.GONE);

    }

    /**
     * The interface On start count down.
     */
    interface IOnStartCountDown {
        /**
         * Start count down.
         *
         * @param timeInMilliSeconds the time in milli seconds
         */
        void startCountDown(long timeInMilliSeconds);

        /**
         * On meeting ended.
         */
        void onMeetingEnded();
    }
}
