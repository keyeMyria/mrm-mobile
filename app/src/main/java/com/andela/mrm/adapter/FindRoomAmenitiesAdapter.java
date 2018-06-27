package com.andela.mrm.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.GetAllRoomsInALocationQuery;
import com.andela.mrm.R;
import com.pchmn.materialchips.ChipView;

import java.util.List;

/**
 * The type Find room adapter.
 */
public class FindRoomAmenitiesAdapter extends RecyclerView.Adapter
        <FindRoomAmenitiesAdapter.ViewHolder> {
    /**
     * The Rooms.
     */
    List<GetAllRoomsInALocationQuery.Resource> amenities;

    /**
     * Instantiates a new Find room adapter.
     *
     * @param amenities the amenities
     */
    public FindRoomAmenitiesAdapter(List<GetAllRoomsInALocationQuery.Resource> amenities) {
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public FindRoomAmenitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                  int viewType) {
        ConstraintLayout cardView = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_amenities_view, parent, false);
        return new FindRoomAmenitiesAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setValues(position);
    }


    @Override
    public int getItemCount() {
        return amenities.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Amenities view.
         */
        ChipView amenitiesView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            amenitiesView = itemView.findViewById(R.id.chip_amenities);

        }

        /**
         * Sets values.
         *
         * @param position the position
         */
        public void setValues(int position) {
            amenitiesView.setLabel(amenities.get(position).name());
        }
    }
}
