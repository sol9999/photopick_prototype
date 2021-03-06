package com.t12.prototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.opencv.imgproc.Imgproc.rectangle;

public class MainActivity extends AppCompatActivity {

    //OpenCV laod용 CallBack
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS ) {
                // now we can call opencv code !
            } else {
                super.onManagerConnected(status);
            }
        }
    };

//implements AutoPermissionsListener
    ProgressDialog asyncDialog;

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

    ArrayList<String> non_tag_images;

    Fragment_Home fragment_home;
    String selected_image_uri; // 선택한 사진의 이미지 경로를 저장해서 다른 프래그먼트들 끼리의 통신을 위한 변수

    Fragment_People fragment_people;

    BottomNavigationView bottomNavigation;

    private long lastTimeBackPressed;

    String view_all_fragment; // 전체보기를 선택한 프래그먼트를 구별하기 위한 변수

    Classifier classifier = new Classifier(this);

    @Override
    public void onBackPressed() {

        // 인스턴스 초기화
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // 백스택이 존재할 떄까지
        if (fm.getBackStackEntryCount() > 0)
        {
            fm.popBackStack();
            // ft.commit();
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

        /* 원본
        // 위험 권환 자동부여 요청
        AutoPermissions.Companion.loadAllPermissions(this, 101);
         */

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

        non_tag_images = new ArrayList<>();

        pathOfAllImages = getPathOfAllImages();

        find_non_tag_image();

        for(String s : non_tag_images) {
            Log.e("non_tag_images", s);
        }

        copyFile("haarcascade_frontalface_default.xml");

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
                                fragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                                fragmentTransaction.replace(R.id.container, fragment_home).commit();
                                return true;

                            case R.id.tab4:
                                FragmentManager fragmentManager2 = getSupportFragmentManager();
                                fragmentManager2.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                                fragmentTransaction2.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                                fragmentTransaction2.replace(R.id.container, fragment_people).commit();

                                return true;
                        }

                        return false;
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("OnResume","시작");
        if(!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV Init", "OpenCV 라이브러리를 찾았습니다.");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        ClassifictaionTask task = new ClassifictaionTask();
        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncDialog != null) {
            asyncDialog.dismiss();
            asyncDialog = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (asyncDialog != null) {
            asyncDialog.dismiss();
            asyncDialog = null;
        }
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
                if(absolutePathOfImage.substring(absolutePathOfImage.lastIndexOf(".")+1).equals("jpg")) {
                    result.add(absolutePathOfImage);
                }
            }
        }

        return result;
    }

    // 태그 없는 이미지(모델에 넣을 이미지) 경로만 따로 저장
    public void find_non_tag_image() {
        for(String filepath : pathOfAllImages) {
            try {
                ExifInterface exif = new ExifInterface(filepath);
                String tag_description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);

                if (tag_description == null) {
                    non_tag_images.add(filepath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // pathofAllImages에 담긴 모든 이미지의 경로를 인물별로 분류해주는 메소드 ( 태그 확인 후 인물 별로 경로 분리 저장 )
    public void imagePathClassification() {
        int others_count = 0;
        for(String filepath : pathOfAllImages) {
            try {
                ExifInterface exif = new ExifInterface(filepath);
                String tag_description = exif.getAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION);
                if(tag_description != null) {
                String tag_split[] = tag_description.split("/");

                    if(tag_split.length == 1) { // 인물 1명일 경우
                        switch (tag_split[0]) {
                            case "person1":
                                person1.add(filepath);
                                person1_alone.add(filepath);
                                break;
                            case "person2":
                                person2.add(filepath);
                                person2_alone.add(filepath);
                                break;
                            case "person3":
                                person3.add(filepath);
                                person3_alone.add(filepath);
                                break;
                            case "person4":
                                person4.add(filepath);
                                person4_alone.add(filepath);
                                break;
                            case "others":
                                others.add(filepath);
                                break;
                            default:
                                break;
                        }
                    }
                    else { // 인물 2인 이상일 경우
                        for(int i=0;i<tag_split.length;i++) {
                            switch (tag_split[i]) {
                                case "person1":
                                    person1.add(filepath);
                                    person1_together.add(filepath);
                                    break;
                                case "person2":
                                    person2.add(filepath);
                                    person2_together.add(filepath);
                                    break;
                                case "person3":
                                    person3.add(filepath);
                                    person3_together.add(filepath);
                                    break;
                                case "person4":
                                    person4.add(filepath);
                                    person4_together.add(filepath);
                                    break;
                                case "others":
                                    if(others_count == 0) {
                                        others.add(filepath);
                                        others_count = 1;
                                    }
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

    public void clear_arraylist() { // 태그 수정 후 재분류를 위해 저장됐었던 인물 경로 초기화
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

    public void detect_and_classification() { // 이미지에서 얼굴 찾은 후 크롭하여 모델에 넣은 후, 결과를 토대로 태그를 만들어 저장함
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        CascadeClassifier cascade = new CascadeClassifier(path+"/haarcascade_frontalface_default.xml");

        Mat image = new Mat();
        Mat resizingGray = new Mat();
        MatOfRect faces = new MatOfRect();
        Bitmap bitmap;
        int count = 0;
        // int test_count = 0; //

        for(String filepath : non_tag_images) {
            String classified_tag = "";
            try {
                    bitmap = Glide.with(this)
                            .asBitmap()
                            .load(filepath)
                            .submit()
                            .get();

                    Utils.bitmapToMat(bitmap, image);

                Imgproc.cvtColor(image,resizingGray,Imgproc.COLOR_BGR2GRAY);
                //Imgproc.resize(resizingGray,resizingGray,new Size(320,180));
                //cascade.detectMultiScale(resizingGray,faces,1.3,3,0,new Size(40,40));

                cascade.detectMultiScale(resizingGray,faces,1.3,3,0);

                for(int i=0;i<faces.total();i++) {
                    String result;
                    Rect rc = faces.toList().get(i);
                    Mat cropped = new Mat(image,rc);
                    Bitmap cropped_bitmap = Bitmap.createBitmap( cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(cropped, cropped_bitmap);
                    cropped_bitmap = Bitmap.createScaledBitmap(cropped_bitmap,128,128,true);
                    result = classifier.classify(cropped_bitmap);
                    Log.e("result", result);
                    // TEST : 크롭 비트맵 저장용
                    //test_count++; //
                    //SaveBitmapToFileCache(cropped_bitmap, "/storage/emulated/0/Pictures/TEST_CROP", test_count*10 + i + ".jpg");

                    //classified_tag += classifier.classify(cropped_bitmap);
                    switch (result) {
                        case "person1":
                            if(classified_tag.equals("")) {
                                classified_tag += "person1";
                            }
                            else {
                                classified_tag += "/person1";
                            }
                                break;
                        case "person2":
                            if(classified_tag.equals("")) {
                                classified_tag += "person2";
                            }
                            else {
                                classified_tag += "/person2";
                            }
                                break;
                        case "person3":
                            if(classified_tag.equals("")) {
                                classified_tag += "person3";
                            }
                            else {
                                classified_tag += "/person3";
                            }
                                break;
                        case "person4":
                            if(classified_tag.equals("")) {
                                classified_tag += "person4";
                            }
                            else {
                                classified_tag += "/person4";
                            }
                                break;
                        case "others":
                            if(classified_tag.equals("")) {
                                classified_tag += "others";
                            }
                            else {
                                classified_tag += "/others";
                            }
                                break;
                        default:
                                break;
                    }
                    Log.e("classified_tag", classified_tag);
                    Log.e("filepath", filepath);
                }
            } catch ( final ExecutionException e) { Log.e("Bitmap Error", e.getMessage()); }
            catch( final InterruptedException e) { Log.e("Bitmap Error", e.getMessage()); }

            try {
                ExifInterface exif = new ExifInterface(filepath);
                exif.setAttribute(ExifInterface.TAG_IMAGE_DESCRIPTION, classified_tag);
                exif.saveAttributes();
            } catch(IOException e) { e.printStackTrace(); }


            bitmap = null;
            count++;
            asyncDialog.setProgress(count);
        }
    }

    // 얼굴인식/분류 작업 진행 AsyncTask
    public class ClassifictaionTask extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            /* 원본
                asyncDialog = new ProgressDialog(MainActivity.this);
                asyncDialog.setCancelable(false);
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.setMessage("인식 분류 작업중...");
                asyncDialog.show();

             */

            asyncDialog = new ProgressDialog(MainActivity.this);
            asyncDialog.setCancelable(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMax(non_tag_images.size());
            asyncDialog.setProgress(0);
            asyncDialog.setMessage("인식 분류 작업중...");
            asyncDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            detect_and_classification();
            imagePathClassification();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            asyncDialog.dismiss();
            super.onPostExecute(aVoid);
        }
    }

    // assets의 haarcascade 를 내부 저장소로 복사해주는 메소드
    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d( "copyFileMethod", "copyFile :: 다음 경로로 파일복사 "+ pathDir);
            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            Log.d("copyFileMethod", "copyFile :: 파일 복사 중 예외 발생 "+e.toString() );
        }
    }

    // CROP Image TEST용 메소드
    /*
    public  void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath,
                                       String filename) {
        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }

        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     */


}