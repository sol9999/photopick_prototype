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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_hometab, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_hometab);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        Image_hometab_Adapter adapter = new Image_hometab_Adapter(getActivity());


        for(String imageuri : ((MainActivity)getActivity()).pathOfAllImages) {
            adapter.addItem(new Image_hometab(imageuri));
        }

        recyclerView.setAdapter(adapter);

        return rootView;
    }
}