package com.atta.eproperty.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.atta.eproperty.R;

import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

    private Context mContext;

    private List<Bitmap> bitmaps;

    public ImagesAdapter(Context mContext, List<Bitmap> bitmaps) {
        this.mContext = mContext;
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final Bitmap bitmap = bitmaps.get(i) ;

        myViewHolder.imageView.setImageBitmap(bitmap);

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(mContext, PropertyDetailsActivity.class);
                //mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_recycler_item);
        }
    }
}
