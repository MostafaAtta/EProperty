package com.atta.eproperty.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.atta.eproperty.R;
import com.atta.eproperty.fragments.FragmentsContract;
import com.atta.eproperty.fragments.FragmentsPresenter;
import com.atta.eproperty.property_datils.PropertyDetailsActivity;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.MyViewHolder> {

    private Context mContext;

    private List<Property> properties;

    private FragmentsPresenter fragmentsPresenter;

    private FragmentsContract.View view ;

    public PropertiesAdapter(Context mContext, List<Property> properties, FragmentsContract.View view, FragmentsPresenter fragmentsPresenter) {
        this.mContext = mContext;
        this.properties = properties;
        this.fragmentsPresenter = fragmentsPresenter;
        this.view = view;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.propery_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Property property = properties.get(i) ;

        final int id = property.getPropertyId();
        final String type = property.getType();
        final String district = property.getDistrict();
        final int price = property.getPrice();
        final int rooms = property.getRooms();
        final int baths = property.getBaths();
        String[] images = property.getImagesUrls();
        final String imageURL = images[0];
        fragmentsPresenter.getRetrofitImage(myViewHolder.propertyImage, imageURL);
        myViewHolder.type.setText(type);
        myViewHolder.price.setText(String.valueOf(price) + " EGP");
        myViewHolder.rooms.setText(String.valueOf(rooms));
        myViewHolder.baths.setText(String.valueOf(baths));
        myViewHolder.district.setText(district);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                intent.putExtra("property", property);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return properties.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView price, type, rooms, baths, district;
        public ImageView propertyImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rooms = itemView.findViewById(R.id.rooms_txt);
            propertyImage = itemView.findViewById(R.id.property_image);
            price = itemView.findViewById(R.id.price_txt);
            baths = itemView.findViewById(R.id.baths_txt);
            district = itemView.findViewById(R.id.district_txt);
            type = itemView.findViewById(R.id.type_txt);
        }
    }
}
