package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

// 선택한 이미지를 보여주는 프래그먼트
public class Fragment_ImageSelected extends Fragment {

    public static Fragment_ImageSelected newInstance() {
        return new Fragment_ImageSelected();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.selected_image, container, false);

        ImageView img = (ImageView)rootView.findViewById(R.id.selected_image);
        Glide.with(getActivity()).load(((MainActivity) getActivity()).selected_image_uri).into(img);

        return rootView;
    }
}
