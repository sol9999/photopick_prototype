package com.t12.prototype;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 선택한 이미지를 보여주는 프래그먼트
public class Fragment_ImageSelected extends Fragment {

    public static Fragment_ImageSelected newInstance() {
        return new Fragment_ImageSelected();
    }

    ImageButton tag_mod_btn;
    PhotoView img;
    GestureDetector detector;
    int ui_flag = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.selected_image, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tag_mod_btn = (ImageButton) rootView.findViewById(R.id.tag_mod_button);
        img = (PhotoView)rootView.findViewById(R.id.selected_image);

        Glide.with(getActivity()).load(((MainActivity) getActivity()).selected_image_uri).into(img);

        ((MainActivity) getActivity()).bottomNavigation.setVisibility(View.GONE);
        tag_mod_btn.setVisibility(View.INVISIBLE);

        tag_mod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag_modification();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ui_flag == 1) {
                    img.setBackgroundColor(Color.BLACK);
                    tag_mod_btn.setVisibility(View.INVISIBLE);
                    ui_flag = 0;
                }
                else {
                    img.setBackgroundColor(Color.WHITE);
                    tag_mod_btn.setVisibility(View.VISIBLE);
                    ui_flag = 1;
                }
            }
        });

        return rootView;
    }

    // 태그 수정 Dialog
    void tag_modification() {
        final List<String> ListItems = new ArrayList<>();
        ListItems.add("아빠");
        ListItems.add("할아버지");
        ListItems.add("할머니");
        ListItems.add("엄마");
        ListItems.add("그외");
        final CharSequence[] items =  ListItems.toArray(new String[ ListItems.size()]);

        final boolean checked[]= {false, false, false, false, false};

        try {
            ExifInterface exif = new ExifInterface(((MainActivity) getActivity()).selected_image_uri);
            String tag_description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
            if(tag_description != null) {
                String tag_split[] = tag_description.split("/");
                for(int i=0;i<tag_split.length;i++) {
                    switch (tag_split[i]) {
                        case "person1":
                            checked[0] = true;
                            break;
                        case "person2":
                            checked[1] = true;
                            break;
                        case "person3":
                            checked[2] = true;
                            break;
                        case "person4":
                            checked[3] = true;
                            break;
                        case "others":
                            checked[4] = true;
                            break;
                    }
                }
            }
        } catch(IOException e) { e.printStackTrace(); }

        final List SelectedItems  = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("태그 수정");
        builder.setMultiChoiceItems(items, checked,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        if (isChecked) {
                            //사용자가 체크한 경우 리스트에 추가
                            SelectedItems.add(which);
                        } else if (SelectedItems.contains(which)) {
                            //이미 리스트에 들어있던 아이템이면 제거
                            SelectedItems.remove(Integer.valueOf(which));
                        }
                    }
                });
        builder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String msg="\n";
                        String str="";

                        try {
                            ExifInterface exifInterface = new ExifInterface(((MainActivity) getActivity()).selected_image_uri);

                            for (int i = 0; i < SelectedItems.size(); i++) {
                                int index = (int) SelectedItems.get(i);

                                switch (ListItems.get(index)) {
                                    case "아빠":
                                        if(str == "")
                                            str += "person1";
                                        else
                                            str += "/person1";
                                        msg += "#아빠";
                                        break;
                                    case "할아버지":
                                        if(str == "")
                                            str += "person2";
                                        else
                                            str += "/person2";
                                        msg += "#할아버지";
                                        break;
                                    case "할머니":
                                        if(str == "")
                                            str += "person3";
                                        else
                                            str += "/person3";
                                        msg += "#할머니";
                                        break;
                                    case "엄마":
                                        if(str == "")
                                            str += "person4";
                                        else
                                            str += "/person4";
                                        msg += "#엄마";
                                        break;
                                    case "그외":
                                        if(str == "")
                                            str += "others";
                                        else
                                            str += "/others";
                                        msg += "#그외";
                                        break;
                                    default:
                                        break;
                                }
                            }

                            if(SelectedItems.size()>0) {
                                exifInterface.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, str);
                                exifInterface.saveAttributes();
                                ((MainActivity) getActivity()).clear_arraylist();
                                ((MainActivity) getActivity()).imagePathClassification();
                            }

                            Toast.makeText(getActivity(),
                                    "선택한 태그로 수정되었습니다.\n"+ msg , Toast.LENGTH_LONG)
                                    .show();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(),
                                "지원하지 않는 파일입니다.", Toast.LENGTH_LONG)
                                .show();
                            e.printStackTrace();
                        }


                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
