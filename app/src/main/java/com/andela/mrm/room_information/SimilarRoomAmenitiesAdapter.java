package com.andela.mrm.room_information;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andela.mrm.R;
import com.pchmn.materialchips.ChipView;

import java.util.List;

/**
 * The type Similar room amenities adapter.
 */
public class SimilarRoomAmenitiesAdapter extends
        RecyclerView.Adapter<SimilarRoomAmenitiesAdapter.ViewHolder> {
    /**
     * The Room amenities.
     */
    List<String> roomAmenities;

    /**
     * Instantiates a new Similar room amenities adapter.
     *
     * @param roomAmenities the room amenities
     */
    public SimilarRoomAmenitiesAdapter(List<String> roomAmenities) {
        this.roomAmenities = roomAmenities;
    }

    @NonNull
    @Override
    public SimilarRoomAmenitiesAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_single_amenity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SimilarRoomAmenitiesAdapter.ViewHolder holder, int position) {
        holder.chip.setLabel(roomAmenities.get(position));
    }

    @Override
    public int getItemCount() {
        return roomAmenities.size();
    }

    /**
     * The type View holder.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Chip.
         */
        ChipView chip;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);

            chip = itemView.findViewById(R.id.single_chip);
        }
    }
}
