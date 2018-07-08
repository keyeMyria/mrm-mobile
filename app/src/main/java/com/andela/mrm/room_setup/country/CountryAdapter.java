package com.andela.mrm.room_setup.country;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andela.mrm.AllLocationsQuery;
import com.andela.mrm.R;
import com.andela.mrm.room_setup.building.BuildingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * Country adapter class which extends the recycler view.
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    public final Context context;
    public final List<AllLocationsQuery.AllLocation> countries;

    /**
     * Country adapter constructor.
     * @param context - current activity layout
     * @param countries - list of countries
     */

    public CountryAdapter(Context context, List<AllLocationsQuery.AllLocation> countries) {
        this.context = context;
        this.countries = countries;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_country_button, parent, false);
        return new CountryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CountryAdapter.ViewHolder holder, final int position) {
        final AllLocationsQuery.AllLocation country = countries.get(position);

        holder.countryName.setText(country.name());

        Glide.with(context)
                .load(country.imageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.flag_placeholder)
                )
                .into(holder.button);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BuildingActivity.class);
                intent.putExtra("countryID", String.valueOf(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    /**
     * view holder class.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton button;
        public TextView countryName;

        /**
         * method for picking the parameters.
         * @param view - current view
         */
        public ViewHolder(View view) {
            super(view);
            button = view.findViewById(R.id.country_button);
            countryName = view.findViewById(R.id.country_name);
        }

    }
}
