package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

// People 탭
public class Fragment_People extends Fragment {

    public static Fragment_People newInstance() {
        return new Fragment_People();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_peopletab, container, false);

        ImageView imageView_person1 = (ImageView)rootView.findViewById(R.id.imageView_person1);
        ImageView imageView_person2 = (ImageView)rootView.findViewById(R.id.imageView_person2);
        ImageView imageView_person3 = (ImageView)rootView.findViewById(R.id.imageView_person3);
        ImageView imageView_person4 = (ImageView)rootView.findViewById(R.id.imageView_person4);


        // 해당 인물 사진 존재 시, 첫번째 사진으로 폴더 이미지 설정 ( 없을 경우, drawble의 no_photo_icon 으로 설정 )
        if( !((MainActivity)getActivity()).person1.isEmpty() ) {
            Glide.with(getActivity()).load(((MainActivity) getActivity()).person1.get(0)).centerCrop().into(imageView_person1);
        }
        else {
            imageView_person1.setImageResource(R.drawable.no_photo_icon);
        }

        if( !((MainActivity)getActivity()).person2.isEmpty() ) {
            Glide.with(getActivity()).load(((MainActivity) getActivity()).person2.get(0)).centerCrop().into(imageView_person2);
        }
        else {
            imageView_person2.setImageResource(R.drawable.no_photo_icon);
        }

        if( !((MainActivity)getActivity()).person3.isEmpty() ) {
            Glide.with(getActivity()).load(((MainActivity) getActivity()).person3.get(0)).centerCrop().into(imageView_person3);
        }
        else {
            imageView_person3.setImageResource(R.drawable.no_photo_icon);
        }

        if( !((MainActivity)getActivity()).person4.isEmpty() ) {
            Glide.with(getActivity()).load(((MainActivity) getActivity()).person4.get(0)).centerCrop().into(imageView_person4);
        }
        else {
            imageView_person4.setImageResource(R.drawable.no_photo_icon);
        }



        CardView cardView_person1 = (CardView)rootView.findViewById(R.id.CardView_person1);
        CardView cardView_person2 = (CardView)rootView.findViewById(R.id.CardView_person2);
        CardView cardView_person3 = (CardView)rootView.findViewById(R.id.CardView_person3);
        CardView cardView_person4 = (CardView)rootView.findViewById(R.id.CardView_person4);

        // 각 인물 카드뷰 클릭 시, 인물 프래그먼트로 넘어감
        cardView_person1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(Fragment_person1.newInstance());
            }
        });

        cardView_person2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(Fragment_person2.newInstance());
            }
        });

        cardView_person3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(Fragment_person3.newInstance());
            }
        });

        cardView_person4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragment(Fragment_person4.newInstance());
            }
        });

        return rootView;
    }
}