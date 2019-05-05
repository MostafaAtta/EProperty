package com.atta.eproperty.model;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atta.eproperty.R;
import com.atta.eproperty.property_datils.PropertyDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.MyViewHolder> {

    private Context mContext;

    private List<Property> properties;

    public PropertiesAdapter(Context mContext, List<Property> properties) {
        this.mContext = mContext;
        this.properties = properties;
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

        final int id = property.getId();
        final String type = property.getType();
        switch (type){
            case "Land":
            case "Garage":
            case "Building":
                myViewHolder.roomsLinearLayout.setVisibility(View.GONE);
                myViewHolder.bathsLinearLayout.setVisibility(View.GONE);
                break;
            default:
                myViewHolder.roomsLinearLayout.setVisibility(View.VISIBLE);
                myViewHolder.bathsLinearLayout.setVisibility(View.VISIBLE);
                break;
        }
        final String district = property.getDistrict();
        final int price = property.getPrice();
        final int rooms = property.getRooms();
        final int baths = property.getBaths();
        String[] images = property.getImagesUrls();
        if (property.getImagesUrls() != null){

            final String imageURL = APIUrl.Images_BASE_URL + images[0];
            Picasso.get()
                    .load(imageURL)
                    .resize(50, 50)
                    .centerCrop()
                    .into(myViewHolder.propertyImage);
        }
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
        public LinearLayout roomsLinearLayout, bathsLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rooms = itemView.findViewById(R.id.rooms_txt);
            propertyImage = itemView.findViewById(R.id.property_image);
            price = itemView.findViewById(R.id.price_txt);
            baths = itemView.findViewById(R.id.baths_txt);
            district = itemView.findViewById(R.id.district_txt);
            type = itemView.findViewById(R.id.type_txt);
            roomsLinearLayout = itemView.findViewById(R.id.rooms_layout);
            bathsLinearLayout = itemView.findViewById(R.id.baths_layout);
        }
    }
}
