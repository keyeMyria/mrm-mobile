package com.andela.mrm.room_setup.floor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.R;
import com.andela.mrm.room_setup.meeting_room.MeetingRoomActivity;

import java.util.List;

/**
 * Created by isioyemohammed on 12/04/2018.
 * mrm-mobile
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> {
    public final Context context;
    private final List<AllLocationsQuery.Floor> floors;
    final String countryId;
    final String buildingId;

    /**
     * Floor adapter constructor.
     *
     * @param context    - Instance of the current context
     * @param floors     - List of floors
     * @param countryId  - id for currently selected country
     * @param buildingId - id for currently selected building
     */
    public FloorAdapter(Context context, List<AllLocationsQuery.Floor> floors,
                        String countryId, String buildingId) {
        this.context = context;
        this.floors = floors;
        this.countryId = countryId;
        this.buildingId = buildingId;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return floors.size();
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
     * Since it will be re-used to display
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
    public FloorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_floor_button, parent, false);
        return new ViewHolder(view);
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
     * Override {onBindViewHolder} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final FloorAdapter.ViewHolder holder,
                                 final int position) {
        final AllLocationsQuery.Floor floor = floors.get(position);
        holder.button.setText(floor.name());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MeetingRoomActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("countryID", countryId);
                bundle.putString("buildingID", buildingId);
                bundle.putString("floorID", String.valueOf(holder.getAdapterPosition()));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return 0;
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
            button = view.findViewById(R.id.floor_button);
        }
    }
}
