package com.atta.eproperty.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atta.eproperty.R;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.MyViewHolder> {

    String[] amenitiesArray;

    public AmenitiesAdapter(String[] amenitiesArray) {
        this.amenitiesArray = amenitiesArray;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.amenities_grid_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.view.setText(amenitiesArray[i]);
    }

    @Override
    public int getItemCount() {
        return amenitiesArray.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView view;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.tv_amenities);
        }
    }
}
