package com.ticcorp.ticsong;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ticcorp.ticsong.model.CustomPreference;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;


public class TutorialActivity extends Activity {

    ApplicationClass appClass;

    CustomPreference pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        appClass = (ApplicationClass) getApplication();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        viewPager.setAdapter(imageAdapter);

        pref = pref.getInstance(this.getApplicationContext());
    }


    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (pref.getValue("tutorial", 2) < 2 ) { // 튜토리얼을 처음 보는 경우
            startActivity(new Intent(TutorialActivity.this, MainActivity.class));
            this.finish();
        } else { // 설정에서 튜토리얼로 들어온 경우
            this.finish();
        }
    }

    @Override
    protected void onPause() { // 화면이 가려졌을 때
        super.onPause();
        appClass.bgmPause();
    }

    @Override
    protected void onResume() { // 화면으로 돌아왔을 때
        super.onResume();
        appClass.bgmResume();
    }
}
