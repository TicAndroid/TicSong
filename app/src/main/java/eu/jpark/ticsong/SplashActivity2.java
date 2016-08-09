package eu.jpark.ticsong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by heyhe on 2016-07-30.
 */
public class SplashActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        final ImageView iv = (ImageView)findViewById(R.id.ani_off);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim);
        iv.startAnimation(anim);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);
    }
    private class splashhandler implements Runnable {
        public void run() {
            startActivity(new Intent(getApplication(), LoginActivity.class)); // 로딩이 끝난후 이동할 Activity
            SplashActivity2.this.finish(); // 로딩페이지 Activity Stack에서 제거
        }
    }
}
