package com.andela.mrm.room_information.similar_rooms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.mrm.GetRoomsInABlockQuery;
import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;

import java.util.List;


/**
 * The type Similar room adapter.
 */
public class SimilarRoomAdapter extends RecyclerView.Adapter<SimilarRoomAdapter.ViewHolder> {

    private List<GetRoomsInABlockQuery.Room> mRooms;

    /**
     * Instantiates a new Similar room adapter.
     *
     * @param rooms the room amenites list
     */
    public SimilarRoomAdapter(List<GetRoomsInABlockQuery.Room> rooms) {
        mRooms = rooms;
    }

    /**
     * Set rooms method.
     * @param mRooms - mRooms
     */
    public void setRooms(List<GetRoomsInABlockQuery.Room> mRooms) {
        this.mRooms = mRooms;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SimilarRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_similar_room_card, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRoomAdapter.ViewHolder holder, int position) {
        holder.bind(mRooms.get(position));
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }


    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView roomName;
        TextView floor;
        TextView capacity;
        /**
         * The M recycler view.
         */
        RecyclerView mRecyclerView;


        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);
            roomName = itemView.findViewById(R.id.text_room_name_activity_room_info);
            floor = itemView.findViewById(R.id.text_room_location_activity_room_info);
            capacity = itemView.findViewById(R.id.number_attendees);

            mRecyclerView = itemView.findViewById(R.id.amenites_recycler_view);
            mRecyclerView.setLayoutManager(
                    new StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL));
        }

        /**
         * Bind method for room.
         *
         * @param room - room
         */
        public void bind(GetRoomsInABlockQuery.Room room) {
            Room roomFragment = room.fragments().room();
            String location = roomFragment.floor().name() + ", "
                    + roomFragment.floor().block().name();

            roomName.setText(roomFragment.name());
            floor.setText(location);
            capacity.setText(String.valueOf(roomFragment.capacity()));

            mRecyclerView.setAdapter(new SimilarRoomAmenitiesAdapter(roomFragment.resources()));
        }
    }

}

