package com.t12.prototype;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class SplashActivity extends Activity implements AutoPermissionsListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 위험 권환 자동부여 요청
        AutoPermissions.Companion.loadAllPermissions(this, 101);

        /*
        try {
            Thread.sleep(2000); // 대기 시간 설정
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
        */
    }

    // 위험 권환 부여 응답 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean check_result = true;

        // 모든 퍼미션을 허용했는지 체크
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                check_result = false;
                break;
            }
        }

        if(check_result == false) {
            Toast.makeText(this,"앱을 다시 실행하여 권한을 허용해주세요.",Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            try {
                Thread.sleep(2000); // 대기 시간 설정
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}
