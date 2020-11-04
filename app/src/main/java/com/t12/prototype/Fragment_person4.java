package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_person4 extends Fragment {

    public static Fragment_person4 newInstance() {
        return new Fragment_person4();
    }

    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    Image_hometab_Adapter adapter1;
    Image_hometab_Adapter adapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_person, container, false);

        recyclerView1 = rootView.findViewById(R.id.recyclerView_albumtab_1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView_albumtab_2);

        TextView person_fragment_title_textView;
        person_fragment_title_textView = rootView.findViewById(R.id.person_fragment_title);
        person_fragment_title_textView.setText("Andrew");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter1 = new Image_hometab_Adapter(getActivity());
        adapter2 = new Image_hometab_Adapter(getActivity());

        if(((MainActivity)getActivity()).person4_alone != null) {
            for (String imageuri : ((MainActivity) getActivity()).person4_alone) {
                adapter1.addItem(new Image_hometab(imageuri));
            }
        }
        else {
            // 리싸이클러뷰에 채울 아이템이 없을때 채울 것 생각.
        }

        if(((MainActivity)getActivity()).person4_together != null) {
            for (String imageuri : ((MainActivity) getActivity()).person4_together) {
                adapter2.addItem(new Image_hometab(imageuri));
            }
        }
        else {
            // 리싸이클러뷰에 채울 아이템이 없을때 채울 것 생각.
        }
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);

        adapter1.setOnItemClickListener(new OnGalleryItemClickListener() {
            @Override
            public void onItemClick(Image_hometab_Adapter.ViewHolder holder, View view, int position) {
                Image_hometab item = adapter1.getItem(position);
                ((MainActivity)getActivity()).selected_image_uri = item.getImageURI();
                ((MainActivity)getActivity()).replaceFragment(Fragment_ImageSelected.newInstance());
            }
        });

        adapter2.setOnItemClickListener(new OnGalleryItemClickListener() {
            @Override
            public void onItemClick(Image_hometab_Adapter.ViewHolder holder, View view, int position) {
                Image_hometab item = adapter2.getItem(position);
                ((MainActivity)getActivity()).selected_image_uri = item.getImageURI();
                ((MainActivity)getActivity()).replaceFragment(Fragment_ImageSelected.newInstance());
            }
        });


        return rootView;
    }
}
