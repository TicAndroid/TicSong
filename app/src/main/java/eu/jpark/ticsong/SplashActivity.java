package eu.jpark.ticsong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import eu.jpark.ticsong.activitySupport.DBHelper;


/**
 * Created by heyhe on 2016-07-30.
 */
public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs;
    final String firstRunPrefs = "firstRun";

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


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

        /* Init DB */
        dbHelper = new DBHelper(this);
        Log.e("DBHelper",dbHelper.toString());

        dbHelper.insert("TestID", "TestName");
        Cursor c = dbHelper.retrieve("TestID");
        c.moveToFirst();
        if (c != null) {

            Log.e("DBHelper",c.getString(c.getColumnIndex("userId")));
        }


        final ImageView iv = (ImageView)findViewById(R.id.ani_on);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim_opposite);
        iv.startAnimation(anim);

        final ImageView iv2 = (ImageView)findViewById(R.id.ani_off);
        //Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim);
        //iv.startAnimation(anim2);


        iv2.setVisibility(View.VISIBLE);
        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 2500);
    }
    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }


}
