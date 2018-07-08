package com.andela.mrm.room_events;

import java.text.DateFormat;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
    /**
     * The Calendar events.
     */
    public final List<CalendarEvent> calendarEvents;
    /**
     * The Context.
     */
    public final Context context;


    /**
     * Instantiates a new Event schedule adapter.
     *
     * @param events  the events
     * @param context the context
     */
    public EventScheduleAdapter(List<CalendarEvent> events, Context context) {
        this.calendarEvents = events;
        this.context = context;
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout)
                LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_event_schedule_view, parent, false);
        return new ViewHolder(constraintLayout);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(ViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.e("Position", position + "");

        holder.setValue(position);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return calendarEvents.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Event title.
         */
        public TextView eventTitle, /**
         * The Start time.
         */
        startTime, /**
         * The Duration.
         */
        duration, /**
         * The No of attendees view.
         */
        noOfAttendeesView;
        /**
         * The Attendees recycler view.
         */
        RecyclerView attendeesRecyclerView;

        /**
         * The Static image participants.
         */
        public ImageView staticImageParticipants, /**
         * The Close recycler view.
         */
        closeRecyclerView;

        /**
         * The Availability indicator.
         */
        public View availabilityIndicator;
        /**
         * The Is minute.
         */
        public Boolean isMinute = false;
        /**
         * The Constraint layout.
         */
        public ConstraintLayout constraintLayout;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.text_event_title);
            startTime = itemView.findViewById(R.id.text_meeting_time);
            duration = itemView.findViewById(R.id.text_event_duration);
            availabilityIndicator = itemView.findViewById(R.id.view_indicator);
            noOfAttendeesView = itemView.findViewById(R.id.text_attendees);
            staticImageParticipants = itemView.findViewById(R.id.staticImage);
            attendeesRecyclerView = itemView.findViewById(R.id.list_attendees_recyclerview);
            closeRecyclerView = itemView.findViewById(R.id.close_recycler_view);
            constraintLayout = itemView.findViewById(R.id.constraintLayout3);
        }

        /**
         * Sets value.
         *
         * @param position the position
         */
        public void setValue(final int position) {
            String extension;
            if (calendarEvents.get(position).getEndTime() == null) {
                duration.setText("All day");
                eventTitle.setText("Free Till End Of Day");
                startTime.setText(formatTime(calendarEvents.get(position).getStartTime(),
                        "GMT+1", false));
            } else {
                Long end = calendarEvents.get(position).getEndTime();
                Long start  = calendarEvents.get(position).getStartTime();
                getEventAttendees(position);
                Long diff = end - start;
                eventTitle.setText(calendarEvents.get(position).getSummary());
                startTime.setText(formatTime(start, "GMT+1", false));
                String format = formatTime(diff, "GMT", true);
                if (isMinute) {
                    extension = "min";
                } else {
                    extension = "hr";
                }
                duration.setText(format + extension);
            }
            if ("Available".equals(calendarEvents.get(position).getSummary())) {
                availabilityIndicator.setBackgroundColor(Color.GREEN);
            } else {
                availabilityIndicator.setBackgroundColor(Color.RED);
            }

        }

        /**
         * Extracted Method to deal with obtaining event Attendees.
         * @param position integer value of attendees
         */
        public void getEventAttendees(final int position) {
            if (calendarEvents.get(position).getAttendees() != null) {
                final int noOfAttendees = calendarEvents.get(position).getAttendees().size();
                noOfAttendeesView.setOnClickListener(v -> showAttendees(position));

                closeRecyclerView.setOnClickListener(v -> hideAttendees(noOfAttendees));

                noOfAttendeesView.setText(new StringBuilder().append(noOfAttendees)
                        .append(" Participants").toString());
                staticImageParticipants.setVisibility(View.VISIBLE);

                AttendeesAdapter attendeesAdapter =
                        new AttendeesAdapter(calendarEvents.get(position).getAttendees());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false);
                attendeesRecyclerView.setLayoutManager(layoutManager);
                attendeesRecyclerView.setAdapter(attendeesAdapter);
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
        public String formatTime(Long timeValue, String timeZone, Boolean isTimeDiff) {
            DateFormat format;
            Long hour = 3600000L;
            Date date = new Date(timeValue);
            if (isTimeDiff) {
                if (timeValue < hour) {
                    format = new SimpleDateFormat("mm ", Locale.getDefault());
                    isMinute = true;
                } else {
                    format = new SimpleDateFormat("h:mm ", Locale.getDefault());
                    isMinute = false;
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
        public void showAttendees(int position) {
            availabilityIndicator.setVisibility(View.GONE);
            eventTitle.setTextColor(Color.WHITE);
            duration.setTextColor(Color.WHITE);
            noOfAttendeesView.setTextColor(Color.WHITE);
            attendeesRecyclerView.setVisibility(View.VISIBLE);
            closeRecyclerView.setVisibility(View.VISIBLE);
            staticImageParticipants.setVisibility(View.GONE);
            noOfAttendeesView.setText(calendarEvents.get(position).getCreator());
            constraintLayout.
                    setBackgroundColor(Color.parseColor("#FFFF5359"));
        }

        /**
         * Modify layout to hide attendees list.
         *
         * @param noOfAttendees the number of attendees.
         */
        public void hideAttendees(int noOfAttendees) {
            availabilityIndicator.setVisibility(View.VISIBLE);
            eventTitle.setTextColor(Color.BLACK);
            duration.setTextColor(Color.BLACK);
            noOfAttendeesView.setTextColor(Color.BLUE);
            attendeesRecyclerView.setVisibility(View.GONE);
            closeRecyclerView.setVisibility(View.GONE);
            staticImageParticipants.setVisibility(View.VISIBLE);
            noOfAttendeesView.setText(new StringBuilder().append(noOfAttendees)
                    .append(" Participants").toString());
            constraintLayout.setBackgroundColor(Color.WHITE);
        }
    }
}
