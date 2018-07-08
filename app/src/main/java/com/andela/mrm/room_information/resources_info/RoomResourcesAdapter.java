package com.andela.mrm.room_information.resources_info;

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
 * Room Resources Adapter Class.
 */
public class RoomResourcesAdapter extends RecyclerView.Adapter<RoomResourcesAdapter.ViewHolder> {

    private List<Room.Resource> mResources;

    /**
     * Instantiates a new Room resources adapter.
     *
     * @param resources the resources
     */
    public RoomResourcesAdapter(List<Room.Resource> resources) {
        mResources = resources;
    }

    @NonNull
    @Override
    public RoomResourcesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room_resource, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomResourcesAdapter.ViewHolder holder, int position) {
        holder.bind(mResources.get(position));
    }

    @Override
    public int getItemCount() {
        return mResources.size();
    }

    /**
     * Sets resources and calls notifyDataSetChanged.
     *
     * @param resources the resources
     */
    public void setResources(List<Room.Resource> resources) {
        mResources = resources;
        notifyDataSetChanged();
    }

    /**
     * View holder for each resource item.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The M resource name.
         */
        TextView mResourceName;

        /**
         * The M resource quantity.
         */
        TextView mResourceQuantity;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);
            mResourceName = itemView.findViewById(R.id.text_resource_name_room_info);
            mResourceQuantity = itemView.findViewById(R.id.text_resource_quantity_room_info);
        }

        /**
         * Bind.
         *
         * @param resource the resource
         */
        public void bind(Room.Resource resource) {
            mResourceName.setText(resource.name());
            // TODO: implement set quantity when data is available from the backend
//            mResourceQuantity.setText(resource.quantity());
        }
    }
}
