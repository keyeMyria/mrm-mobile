package com.andela.mrm.room_setup.building;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.R;
import com.andela.mrm.room_setup.floor.FloorSelectionActivity;

import java.util.List;

//import com.andela.mrm.room_booking.building.BuildingActivity;

/**
 * Created by baron on 20/04/2018.
 */

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.ViewHolder> {

    public final Context context;
    final String countryId;
    private final List<AllLocationsQuery.Block> buildings;

    /**
     * Building adapter constructor.
     *
     * @param context   - Instance of the current context
     * @param buildings - List of buildings
     * @param countryId - Country ID
     */
    public BuildingAdapter(Context context, List<AllLocationsQuery.Block> buildings,
                           String countryId) {
        this.context = context;
        this.buildings = buildings;
        this.countryId = countryId;
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_building_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BuildingAdapter.ViewHolder holder, final int position) {
        final AllLocationsQuery.Block building = buildings.get(position);

        holder.button.setText(building.name());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FloorSelectionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("countryID", countryId);
                bundle.putString("buildingID", String.valueOf(position));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    /**
     * view holder class.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        /**
         * @param view method for picking the parameters.
         */
        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.building_button);
        }

    }
}
