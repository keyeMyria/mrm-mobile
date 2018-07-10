package com.andela.mrm.room_information.similar_rooms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.mrm.R;
import com.andela.mrm.fragment.Room;

import java.util.List;

/**
 * The type Similar room amenities adapter.
 */
public class SimilarRoomAmenitiesAdapter extends
        RecyclerView.Adapter<SimilarRoomAmenitiesAdapter.ViewHolder> {
    /**
     * The Room amenities.
     */
    List<Room.Resource> mResources;

    /**
     * Instantiates a new Similar room amenities adapter.
     *
     * @param resources the room amenities
     */
    public SimilarRoomAmenitiesAdapter(List<Room.Resource> resources) {
        mResources = resources;
    }

    @NonNull
    @Override
    public SimilarRoomAmenitiesAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_single_amenity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SimilarRoomAmenitiesAdapter.ViewHolder holder, int position) {
        holder.bind(mResources.get(position));
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }

    /**
     * The type View holder.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView resourceNameText;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);
            resourceNameText = itemView.
                    findViewById(R.id.text_resource_name_fragment_similar_rooms);
        }

        /**
         * Bind method for room resources.
         * @param resource - Room resources
         */
        public void bind(Room.Resource resource) {
            // TODO: add resource count when avaiable from backend
            // TODO: String resourceName = resource.name() + "(" + resource.count() + ")";
            resourceNameText.setText(resource.name());
        }
    }
}
