package com.t12.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Image_hometab_Adapter extends RecyclerView.Adapter<Image_hometab_Adapter.ViewHolder> implements OnGalleryItemClickListener{
    ArrayList<Image_hometab> items = new ArrayList<Image_hometab>();
    OnGalleryItemClickListener listener;
    Context context;

    public Image_hometab_Adapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.gallery_item, viewGroup, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull Image_hometab_Adapter.ViewHolder viewHolder, int position) {
        Image_hometab item = items.get(position);
        viewHolder.setContext(context);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Context context;

        public ViewHolder(View itemView, final OnGalleryItemClickListener listener) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });

            imageView = itemView.findViewById(R.id.gallery_image);

        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setItem(Image_hometab item) {

            Glide.with(context).load(item.getImageURI()).centerCrop().into(imageView);
        }
    }

    public void setOnItemClickListener(OnGalleryItemClickListener listener) {
        this.listener = listener;
    }

    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClick(holder, view, position);
        }
    }

    public void addItem(Image_hometab item) {
        items.add(item);
    }

    public void setItems(ArrayList<Image_hometab> items) {
        this.items = items;
    }

    public Image_hometab getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Image_hometab item) {
        items.set(position, item);
    }
}
