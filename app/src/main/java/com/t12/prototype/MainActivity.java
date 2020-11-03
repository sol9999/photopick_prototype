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

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    ArrayList<String> pathOfAllImages;
    ArrayList<String> person1;
    ArrayList<String> person2;
    ArrayList<String> person3;
    ArrayList<String> person4;
    ArrayList<String> others;
    Fragment_Home fragment_home;
    Fragment_People fragment_people;

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
        person2 = new ArrayList<>();
        person3 = new ArrayList<>();
        person4 = new ArrayList<>();
        others = new ArrayList<>();

        pathOfAllImages = getPathOfAllImages();

        imagePathClassification();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).commit();

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.tab3:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_home).commit();

                                return true;

                            case R.id.tab4:
                                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment_people).commit();

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
    private void imagePathClassification() {

        for(String filepath : pathOfAllImages) {
            try {
                ExifInterface exif = new ExifInterface(filepath);
                String tag_description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

                if(tag_description != null) {
                String tag_split[] = tag_description.split("/");

                    switch (tag_split[0]) {
                        case "James":
                            person1.add(filepath);
                            break;
                        case "Alice":
                            person2.add(filepath);
                            break;
                        case "Mike":
                            person3.add(filepath);
                            break;
                        case "Andrew":
                            person4.add(filepath);
                            break;
                        default:
                            others.add(filepath);
                            break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Exif Error!", Toast.LENGTH_LONG).show();
            }
        }
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