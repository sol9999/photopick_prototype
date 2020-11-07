package com.t12.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    ArrayList<String> pathOfAllImages; // 모든 사진 경로 저장

    ArrayList<String> person1; // 모든 person1 사진 경로 저장
    ArrayList<String> person1_alone; // 혼자 , person1 사진 경로 저장
    ArrayList<String> person1_together; // 같이 , person1 사진 경로 저장

    ArrayList<String> person2; // 모든 person1 사진 경로 저장
    ArrayList<String> person2_alone; // 혼자 , person1 사진 경로 저장
    ArrayList<String> person2_together; // 같이 , person1 사진 경로 저장

    ArrayList<String> person3; // 모든 person1 사진 경로 저장
    ArrayList<String> person3_alone; // 혼자 , person1 사진 경로 저장
    ArrayList<String> person3_together; // 같이 , person1 사진 경로 저장

    ArrayList<String> person4; // 모든 person1 사진 경로 저장
    ArrayList<String> person4_alone; // 혼자 , person1 사진 경로 저장
    ArrayList<String> person4_together; // 같이 , person1 사진 경로 저장

    ArrayList<String> others;

    Fragment_Home fragment_home;
    String selected_image_uri; // 선택한 사진의 이미지 경로를 저장해서 다른 프래그먼트들 끼리의 통신을 위한 변수

    Fragment_People fragment_people;

    BottomNavigationView bottomNavigation;

    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {

        // 인스턴스 초기화
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 백스택이 존재할 떄까지
        if (fm.getBackStackEntryCount() > 0)
        {
            fm.popBackStack();
            ft.commit();
        }
        // 백스택이 없는 경우
        else
        {
            //두 번 클릭시 어플 종료
            if(System.currentTimeMillis() - lastTimeBackPressed < 2000){
                finish();
                return;
            }
            lastTimeBackPressed = System.currentTimeMillis();
            Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위험 권환 자동부여 요청
        AutoPermissions.Companion.loadAllPermissions(this, 101);

        //액션바 숨김
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fragment_home = new Fragment_Home();
        fragment_people = new Fragment_People();

        pathOfAllImages = new ArrayList<>();

        person1 = new ArrayList<>();
        person1_alone = new ArrayList<>();
        person1_together = new ArrayList<>();

        person2 = new ArrayList<>();
        person2_alone = new ArrayList<>();
        person2_together = new ArrayList<>();

        person3 = new ArrayList<>();
        person3_alone = new ArrayList<>();
        person3_together = new ArrayList<>();

        person4 = new ArrayList<>();
        person4_alone = new ArrayList<>();
        person4_together = new ArrayList<>();

        others = new ArrayList<>();

        pathOfAllImages = getPathOfAllImages();

        imagePathClassification();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).commit();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        // BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.tab3:
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                                fragmentTransaction.replace(R.id.container, fragment_home).commit();

                                return true;

                            case R.id.tab4:
                                FragmentManager fragmentManager2 = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                                // fragmentTransaction2.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                                fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                                fragmentTransaction2.replace(R.id.container, fragment_people).commit();

                                return true;
                        }

                        return false;
                    }
                }
        );
    }

    // 프래그먼트 to 프래그먼트 화면전환
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(fragment instanceof  Fragment_ImageSelected) {
            fragmentTransaction.setCustomAnimations(R.anim.zoom_enter,R.anim.zoom_exit,R.anim.zoom_enter,R.anim.zoom_exit);
        }

        else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
         }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    // 모든 이미지 경로 받아오기  메소드
    private ArrayList<String> getPathOfAllImages()
    {
        ArrayList<String> result = new ArrayList<>();
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, MediaStore.MediaColumns.DATE_ADDED + " desc");
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int columnDisplayname = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);

        int lastIndex;
        while (cursor.moveToNext())
        {
            String absolutePathOfImage = cursor.getString(columnIndex);
            String nameOfFile = cursor.getString(columnDisplayname);

            lastIndex = absolutePathOfImage.lastIndexOf(nameOfFile);
            lastIndex = lastIndex >= 0 ? lastIndex : nameOfFile.length() - 1;

            if (!TextUtils.isEmpty(absolutePathOfImage))
            {
                result.add(absolutePathOfImage);
            }
        }

        return result;
    }

    // 위험 권환 부여 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }


    // pathofAllImages에 담긴 모든 이미지의 경로를 인물별로 분류해주는 메소드
    public void imagePathClassification() {

        for(String filepath : pathOfAllImages) {
            try {
                ExifInterface exif = new ExifInterface(filepath);
                String tag_description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

                if(tag_description != null) {
                String tag_split[] = tag_description.split("/");
                    if(tag_split.length == 1) {
                        switch (tag_split[0]) {
                            case "James":
                                person1.add(filepath);
                                person1_alone.add(filepath);
                                break;
                            case "Alice":
                                person2.add(filepath);
                                person2_alone.add(filepath);
                                break;
                            case "Mike":
                                person3.add(filepath);
                                person3_alone.add(filepath);
                                break;
                            case "Andrew":
                                person4.add(filepath);
                                person4_alone.add(filepath);
                                break;
                            case "Others":
                                others.add(filepath);
                                break;
                            default:
                                break;
                        }
                    }
                    else {
                        for(int i=0;i<tag_split.length;i++) {
                            switch (tag_split[i]) {
                                case "James":
                                    person1.add(filepath);
                                    person1_together.add(filepath);
                                    break;
                                case "Alice":
                                    person2.add(filepath);
                                    person2_together.add(filepath);
                                    break;
                                case "Mike":
                                    person3.add(filepath);
                                    person3_together.add(filepath);
                                    break;
                                case "Andrew":
                                    person4.add(filepath);
                                    person4_together.add(filepath);
                                    break;
                                case "Others":
                                    others.add(filepath);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Exif Error!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void clear_arraylist() {
        person1.clear();
        person1_alone.clear();
        person1_together.clear();

        person2.clear();
        person2_alone.clear();
        person2_together.clear();

        person3.clear();
        person3_alone.clear();
        person3_together.clear();

        person4.clear();
        person4_alone.clear();
        person4_together.clear();

        others.clear();

    }


    /*
    // 이미지 경로를 통해 이미지의 Exif 정보를 받아오는 메소드
    private void getExif(ExifInterface exif) {
        String myAttribute = getTagString(ExifInterface.TAG_IMAGE_DESCRIPTION, exif);
    }

    // Exif의 tag들을 문자열로 연결해주는 메소드
    private String getTagString(String tag, ExifInterface exif) {
        // return (tag + " : " + exif.getAttribute(tag) + "\n");
        return exif.getAttribute(tag);
    }
     */

}