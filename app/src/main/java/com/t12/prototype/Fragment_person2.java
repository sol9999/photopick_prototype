package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_person2 extends Fragment {

    public static Fragment_person2 newInstance() {
        return new Fragment_person2();
    }

    RecyclerView recyclerView1;
    RecyclerView recyclerView2;
    Image_hometab_Adapter adapter1;
    Image_hometab_Adapter adapter2;
    TextView alone_view_all_text;
    TextView together_view_all_text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).bottomNavigation.setVisibility(View.VISIBLE);

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_person, container, false);

        alone_view_all_text = rootView.findViewById(R.id.alone_view_all_text);
        together_view_all_text = rootView.findViewById(R.id.together_view_all_text);

        recyclerView1 = rootView.findViewById(R.id.recyclerView_albumtab_1);
        recyclerView2 = rootView.findViewById(R.id.recyclerView_albumtab_2);

        TextView person_fragment_title_textView;
        person_fragment_title_textView = rootView.findViewById(R.id.person_fragment_title);
        person_fragment_title_textView.setText("Alice");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);

        adapter1 = new Image_hometab_Adapter(getActivity());
        adapter2 = new Image_hometab_Adapter(getActivity());

        if(((MainActivity)getActivity()).person2_alone != null) {
            for (String imageuri : ((MainActivity) getActivity()).person2_alone) {
                adapter1.addItem(new Image_hometab(imageuri));
            }
        }

        if(((MainActivity)getActivity()).person2_together != null) {
            for (String imageuri : ((MainActivity) getActivity()).person2_together) {
                adapter2.addItem(new Image_hometab(imageuri));
            }
        }

        if(adapter1.getItemCount()==0) {
            LinearLayout alone_title = rootView.findViewById(R.id.alone_title);
            alone_title.setVisibility(View.GONE);
        }
        if(adapter2.getItemCount()==0) {
            LinearLayout together_title = rootView.findViewById(R.id.together_title);
            together_title.setVisibility(View.GONE);
        }

        // 아이템 갯수 6개 미만이면 전체보기 안보임
        if(adapter1.getItemCount()<6) {
            TextView alone_view_all_text = rootView.findViewById(R.id.alone_view_all_text);
            alone_view_all_text.setVisibility(View.INVISIBLE);
        }

        if(adapter2.getItemCount()<6) {
            TextView together_view_all_text = rootView.findViewById(R.id.together_view_all_text);
            together_view_all_text.setVisibility(View.INVISIBLE);
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

        alone_view_all_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).view_all_fragment = "Alice_alone";
                ((MainActivity) getActivity()).replaceFragment(Fragment_ViewAll.newInstance());
            }
        });

        together_view_all_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).view_all_fragment = "Alice_together";
                ((MainActivity) getActivity()).replaceFragment(Fragment_ViewAll.newInstance());
            }
        });

        return rootView;
    }
}
