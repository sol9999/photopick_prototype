package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Home 탭
public class Fragment_Home extends Fragment {
    RecyclerView recyclerView;
    Image_hometab_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_hometab, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_hometab);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Image_hometab_Adapter(getActivity());


        for(String imageuri : ((MainActivity)getActivity()).pathOfAllImages) {
            adapter.addItem(new Image_hometab(imageuri));
        }

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnGalleryItemClickListener() {
            @Override
            public void onItemClick(Image_hometab_Adapter.ViewHolder holder, View view, int position) {
                Image_hometab item = adapter.getItem(position);
                ((MainActivity)getActivity()).selected_image_uri = item.getImageURI();
                ((MainActivity)getActivity()).replaceFragment(Fragment_ImageSelected.newInstance());
            }
        });

        return rootView;
    }
}