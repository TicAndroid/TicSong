package com.ticcorp.ticsong;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ticcorp.ticsong.activitySupport.DBHelper;

import java.util.ArrayList;


/**
 * Created by heyhe on 2016-07-30.
 */
public class SplashActivity extends Activity {

    SharedPreferences prefs;
    final String firstRunPrefs = "firstRun";

  //  DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("게임 진행을 위한 권한입니다.")
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.GET_ACCOUNTS, Manifest.permission.WAKE_LOCK,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .check();


        Log.d("스플레쉬"," ??? ");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
         /* Check the First Run */
        boolean firstRun = prefs.getBoolean(firstRunPrefs, true);
        /* Is the First Run */
        if(firstRun) {
            Log.d("FirstRun",firstRunPrefs);

            /* Change First Run prefs to FALSE */
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(firstRunPrefs, false);
            editor.commit();
        }


        /* Init DB
        dbHelper = new DBHelper(this);
        Log.e("DBHelper",dbHelper.toString());

        dbHelper.insert("TestID", "TestName");
        Cursor c = dbHelper.retrieve("TestID");
        c.moveToFirst();
        if (c != null) {

            Log.e("DBHelper",c.getString(c.getColumnIndex("userId")));
        }
*/

        final ImageView iv = (ImageView)findViewById(R.id.ani_twinkle);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim);
        iv.startAnimation(anim);    //twinkle~


        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3500);
    }
    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), FBActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //Toast.makeText(SplashActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            //Toast.makeText(SplashActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };


}
