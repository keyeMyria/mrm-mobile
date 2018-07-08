package com.andela.mrm.room_events;


import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.mrm.R;
import com.google.api.services.calendar.model.EventAttendee;

import java.util.List;

/**
 * The type Attendees adapter.
 */
public class AttendeesAdapter extends RecyclerView.Adapter<AttendeesAdapter.ViewHolder> {

    /**
     * The Event attendees.
     */
    public final List<EventAttendee> eventAttendees;

    /**
     * Instantiates a new Attendees adapter.
     *
     * @param eventAttendees the event attendees
     */
    public AttendeesAdapter(List<EventAttendee> eventAttendees) {
        this.eventAttendees = eventAttendees;
    }

    @NonNull
    @Override
    public AttendeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout constraintLayout = (ConstraintLayout)
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.partial_attendees_list, parent, false);
        return new AttendeesAdapter.ViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeesAdapter.ViewHolder holder, int position) {
        holder.setValue(position);
    }

    @Override
    public int getItemCount() {
        return eventAttendees.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The M email view.
         */
        TextView mEmailView;
        /**
         * The M image response.
         */
        ImageView mImageResponse;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            mEmailView = itemView.findViewById(R.id.text_email);
            mImageResponse = itemView.findViewById(R.id.responseImage);
        }

        /**
         * Sets value.
         *
         * @param position the position
         */
        public void setValue(int position) {
            mEmailView.setText(eventAttendees.get(position).getEmail());
            if ("accepted".equals(eventAttendees.get(position).getResponseStatus())) {
                mImageResponse.setImageResource(R.drawable.icon_accepted);
            } else if ("declined".equals(eventAttendees.get(position).getResponseStatus())) {
                mImageResponse.setImageResource(R.drawable.icon_declined);
            } else if ("needsAction".equals(eventAttendees.get(position).getResponseStatus())) {
                mImageResponse.setImageResource(R.drawable.icon_maybe);
            }
        }
    }
}
