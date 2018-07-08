package com.andela.mrm.find_rooms;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andela.mrm.R;

import java.util.List;

/**
 * The type Dropdown filter adapter.
 */
public class DropdownFilterAdapter extends RecyclerView.Adapter<DropdownFilterAdapter.ViewHolder> {
    /**
     * The Texts.
     */
    List<String> texts;

    /**
     * Instantiates a new Dropdown filter adapter.
     *
     * @param texts the texts
     */
    public DropdownFilterAdapter(List<String> texts) {
        this.texts = texts;
    }

    @NonNull
    @Override
    public DropdownFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partial_filter_dropdown_items, parent, false);
        return new DropdownFilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DropdownFilterAdapter.ViewHolder holder, int position) {
        holder.setValues(position);
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Check box text.
         */
        TextView checkBoxText;
        /**
         * The Check box.
         */
        AppCompatCheckBox checkBox;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            checkBoxText = itemView.findViewById(R.id.checkBoxText);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        /**
         * Sets values.
         *
         * @param position the position
         */
        public void setValues(final int position) {
            checkBoxText.setText(texts.get(position));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Log.e("String value", texts.get(position));
                    }
                }
            });
        }
    }
}
