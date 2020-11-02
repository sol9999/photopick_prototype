package com.t12.prototype;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Person_albumtab_Adapter extends RecyclerView.Adapter<Person_albumtab_Adapter.ViewHolder> {
    ArrayList<Person_albumtab> items = new ArrayList<Person_albumtab>();
    int tabnum = 0;

    public Person_albumtab_Adapter(int tabnum) {
        this.tabnum = tabnum;
    }

    @Override
    public int getItemViewType(int position) {
        return this.tabnum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView;
        if(viewType == 3) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            itemView = inflater.inflate(R.layout.person_item, viewGroup, false);
        }
        else {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            itemView = inflater.inflate(R.layout.person_item_searchtab, viewGroup, false);
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Person_albumtab item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
            textView2 = itemView.findViewById(R.id.textView2);
        }

        public void setItem(Person_albumtab item) {
            textView.setText(item.getFolderName());
            textView2.setText(item.getCount());
        }
    }

    public void addItem(Person_albumtab item) {
        items.add(item);
    }

    public void setItems(ArrayList<Person_albumtab>items) {
        this.items = items;
    }

    public Person_albumtab getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, Person_albumtab item) {
        items.set(position, item);
    }

}
