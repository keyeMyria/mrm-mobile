package com.andela.mrm.room_events;

import java.text.DateFormat;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.mrm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by Fred Adewole on 27/04/2018.
 */
public class EventScheduleAdapter extends RecyclerView.Adapter<EventScheduleAdapter.ViewHolder> {
    private List<CalendarEvent> mCalendarEvents;


    /**
     * Instantiates a new Event schedule adapter.
     *
     * @param events  the events
     */
    EventScheduleAdapter(List<CalendarEvent> events) {
        this.mCalendarEvents = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout)
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_event_schedule_view, parent, false);
        return new ViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setValue(position);
    }

    @Override
    public int getItemCount() {
        return mCalendarEvents.size();
    }

    public void setCalendarEvents(List<CalendarEvent> calendarEvents) {
        mCalendarEvents = calendarEvents;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mEventTitle;
        private TextView mStartTime;
        private TextView mDuration;
        private TextView mNoOfAttendeesView;

        private RecyclerView mAttendeeRV;

        private ImageView staticImageParticipants;
        private ImageView mCloseAttendeesList;

        private View mAvailabilityIndicator;

        private Boolean mIsMinute = false;

        private ConstraintLayout mConstraintLayout;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mEventTitle = itemView.findViewById(R.id.text_event_title);
            mStartTime = itemView.findViewById(R.id.text_meeting_time);
            mDuration = itemView.findViewById(R.id.text_event_duration);
            mAvailabilityIndicator = itemView.findViewById(R.id.view_indicator);
            mNoOfAttendeesView = itemView.findViewById(R.id.text_attendees);
            staticImageParticipants = itemView.findViewById(R.id.staticImage);
            mAttendeeRV = itemView.findViewById(R.id.list_attendees_recyclerview);
            mCloseAttendeesList = itemView.findViewById(R.id.close_recycler_view);
            mConstraintLayout = itemView.findViewById(R.id.constraintLayout3);
        }

        /**
         * Sets value.
         *
         * @param position the position
         */
        public void setValue(final int position) {
            String extension;
            if (mCalendarEvents.get(position).getEndTime() == null) {
                mDuration.setText("All day");
                mEventTitle.setText("Free Till End Of Day");
                mStartTime.setText(formatTime(mCalendarEvents.get(position).getStartTime(),
                        "GMT+1", false));
            } else {
                Long end = mCalendarEvents.get(position).getEndTime();
                Long start  = mCalendarEvents.get(position).getStartTime();
                getEventAttendees(position);
                Long diff = end - start;
                mEventTitle.setText(mCalendarEvents.get(position).getSummary());
                mStartTime.setText(formatTime(start, "GMT+1", false));
                String format = formatTime(diff, "GMT", true);
                if (mIsMinute) {
                    extension = "min";
                } else {
                    extension = "hr";
                }
                String text = format + extension;
                mDuration.setText(text);
            }
            if ("Available".equals(mCalendarEvents.get(position).getSummary())) {
                mAvailabilityIndicator.setBackgroundColor(Color.GREEN);
            } else {
                mAvailabilityIndicator.setBackgroundColor(Color.RED);
            }

        }

        /**
         * Extracted Method to deal with obtaining event Attendees.
         * @param position integer value of attendees
         */
        public void getEventAttendees(final int position) {
            if (mCalendarEvents.get(position).getAttendees() != null) {
                final int noOfAttendees = mCalendarEvents.get(position).getAttendees().size();
                mNoOfAttendeesView.setOnClickListener(v -> showAttendees(position));

                mCloseAttendeesList.setOnClickListener(v -> hideAttendees(noOfAttendees));

                mNoOfAttendeesView.setText(String.format("%s Participants", String.valueOf(noOfAttendees)));
                staticImageParticipants.setVisibility(View.VISIBLE);

                AttendeesAdapter attendeesAdapter =
                        new AttendeesAdapter(mCalendarEvents.get(position).getAttendees());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.itemView.getContext(),
                        LinearLayoutManager.VERTICAL, false);
                mAttendeeRV.setLayoutManager(layoutManager);
                mAttendeeRV.setAdapter(attendeesAdapter);
            }
        }

        /**
         * Format time string.
         *
         * @param timeValue  the time value
         * @param timeZone   the time zone
         * @param isTimeDiff the is time diff
         * @return the string
         */
        private String formatTime(Long timeValue, String timeZone, Boolean isTimeDiff) {
            DateFormat format;
            Long hour = 3600000L;
            Date date = new Date(timeValue);
            if (isTimeDiff) {
                if (timeValue < hour) {
                    format = new SimpleDateFormat("mm ", Locale.getDefault());
                    mIsMinute = true;
                } else {
                    format = new SimpleDateFormat("h:mm ", Locale.getDefault());
                    mIsMinute = false;
                }
            } else {
                format = new SimpleDateFormat("h:mm a", Locale.getDefault());
            }
            format.setTimeZone(TimeZone.getTimeZone(timeZone));
            return format.format(date);
        }

        /**
         * Modify layout to display attendees.
         *
         * @param position the position.
         */
        private void showAttendees(int position) {
            mAvailabilityIndicator.setVisibility(View.GONE);
            mEventTitle.setTextColor(Color.WHITE);
            mDuration.setTextColor(Color.WHITE);
            mNoOfAttendeesView.setTextColor(Color.WHITE);
            mAttendeeRV.setVisibility(View.VISIBLE);
            mCloseAttendeesList.setVisibility(View.VISIBLE);
            staticImageParticipants.setVisibility(View.GONE);
            mNoOfAttendeesView.setText(mCalendarEvents.get(position).getCreator());
            mConstraintLayout.
                    setBackgroundColor(Color.parseColor("#FFFF5359"));
        }

        /**
         * Modify layout to hide attendees list.
         *
         * @param noOfAttendees the number of attendees.
         */
        private void hideAttendees(int noOfAttendees) {
            mAvailabilityIndicator.setVisibility(View.VISIBLE);
            mEventTitle.setTextColor(Color.BLACK);
            mDuration.setTextColor(Color.BLACK);
            mNoOfAttendeesView.setTextColor(Color.BLUE);
            mAttendeeRV.setVisibility(View.GONE);
            mCloseAttendeesList.setVisibility(View.GONE);
            staticImageParticipants.setVisibility(View.VISIBLE);
            mNoOfAttendeesView.setText(String.format("%s Participants", String.valueOf(noOfAttendees)));
            mConstraintLayout.setBackgroundColor(Color.WHITE);
        }
    }
}
