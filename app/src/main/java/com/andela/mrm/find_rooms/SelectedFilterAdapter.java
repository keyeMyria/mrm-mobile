package com.andela.mrm.find_rooms;

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
 * The type Selected filters adapter.
 */
public class SelectedFilterAdapter extends RecyclerView.Adapter<SelectedFilterAdapter.ViewHolder> {

    public final List<String> selectedFilters;

    /**
     * Instantiates a new Selected filters adapter.
     *
     * @param selectedFilters the selected filters
     */
    public SelectedFilterAdapter(List<String> selectedFilters) {
        this.selectedFilters = selectedFilters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ConstraintLayout view = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_selected_filter, parent, false);
        return new SelectedFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setValues(position);
    }

    @Override
    public int getItemCount() {
        return selectedFilters.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Chip view.
         */
        public ChipView chipView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            chipView = itemView.findViewById(R.id.chip_filters);
        }

        /**
         * Sets values.
         *
         * @param position the position
         */
        public void setValues(int position) {
            chipView.setLabel(selectedFilters.get(position));
            chipView.setOnDeleteClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chipView.setVisibility(View.GONE);
                }
            });
        }
    }
}
