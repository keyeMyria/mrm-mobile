package com.andela.mrm.room_setup.meeting_room;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.R;
import com.andela.mrm.room_availability.RoomAvailabilityActivity;

import java.util.List;

/**
 * Created by isioyemohammed on 12/04/2018.
 * mrm-mobile
 */

public class MeetingRoomAdapter extends RecyclerView.Adapter<MeetingRoomAdapter.ViewHolder> {
    private final List<AllLocationsQuery.Room> rooms;

    /**
     * Meeting room adapter constructor.
     *
     * @param rooms - List of rooms
     */
    public MeetingRoomAdapter(List<AllLocationsQuery.Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * View holder class for the recycler view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        /**
         * View parameter.
         *
         * @param view View
         */
        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.meeting_room_button);
        }
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * @param parent   -  The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType - The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public MeetingRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_meeting_room_button, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link ViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull MeetingRoomAdapter.ViewHolder holder, int position) {
        final AllLocationsQuery.Room floor = rooms.get(position);
        holder.button.setText(floor.name());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoomAvailabilityActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return rooms.size();
    }
}
