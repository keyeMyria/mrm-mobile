package com.andela.mrm.room_information;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.mrm.R;

import java.util.List;


/**
 * The type Similar room adapter.
 */
public class SimilarRoomAdapter extends RecyclerView.Adapter<SimilarRoomAdapter.ViewHolder> {

    public final List<RoomInformation> roomAmenitesList;
    public final Context mContext;

    /**
     * Instantiates a new Similar room adapter.
     *
     * @param roomAmenitesList the room amenites list
     * @param context          the context
     */
    public SimilarRoomAdapter(List<RoomInformation> roomAmenitesList, Context context) {
        this.roomAmenitesList = roomAmenitesList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public SimilarRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_similar_room_card, parent, false);

        return new ViewHolder(view);
    }


    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Room name.
         */
        TextView roomName, /**
         * The Floor.
         */
        floor;
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
            mRecyclerView = itemView.findViewById(R.id.amenites_recycler_view);
        }

        /**
         * Sets values.
         *
         * @param position the position
         */
        public void setValues(int position) {
            roomName.setText(roomAmenitesList.get(position).getRoomName());
            floor.setText(roomAmenitesList.get(position).getRoomLocation());
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 2);
            mRecyclerView.setLayoutManager(layoutManager);
            SimilarRoomAmenitiesAdapter attendeesAdapter =
                    new SimilarRoomAmenitiesAdapter(roomAmenitesList.get(position).getRoomItem());
            mRecyclerView.setAdapter(attendeesAdapter);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarRoomAdapter.ViewHolder holder, int position) {

        holder.setValues(position);
    }


    @Override
    public int getItemCount() {
        return roomAmenitesList.size();
    }

}

