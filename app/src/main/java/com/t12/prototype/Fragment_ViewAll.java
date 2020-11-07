package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_ViewAll extends Fragment {

    public static Fragment_ViewAll newInstance() {
        return new Fragment_ViewAll();
    }

    RecyclerView recyclerView;
    Image_hometab_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).bottomNavigation.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_hometab, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView_hometab);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new Image_hometab_Adapter(getActivity());

        // 전체보기를 실행한 프래그먼트에 따라 리싸이클러뷰에 다른 아이템을 설정
        switch(((MainActivity) getActivity()).view_all_fragment) {
            case "James_alone":
                for (String imageuri : ((MainActivity) getActivity()).person1_alone) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;
            case "James_together":
                for (String imageuri : ((MainActivity) getActivity()).person1_together) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;

            case "Alice_alone":
                for (String imageuri : ((MainActivity) getActivity()).person2_alone) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;
            case "Alice_together":
                for (String imageuri : ((MainActivity) getActivity()).person2_together) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;

            case "Mike_alone":
                for (String imageuri : ((MainActivity) getActivity()).person3_alone) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;
            case "Mike_together":
                for (String imageuri : ((MainActivity) getActivity()).person3_together) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;

            case "Andrew_alone":
                for (String imageuri : ((MainActivity) getActivity()).person4_alone) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;
            case "Andrew_together":
                for (String imageuri : ((MainActivity) getActivity()).person4_together) {
                    adapter.addItem(new Image_hometab(imageuri));
                }
                break;
            default:
                break;
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
