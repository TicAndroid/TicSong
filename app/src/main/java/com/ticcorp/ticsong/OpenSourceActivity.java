package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.ticcorp.ticsong.model.CustomPreference;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-09-05.
 */
public class OpenSourceActivity extends Activity {

    ApplicationClass appClass;

    CustomPreference pref;

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;

    public Animation btn_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);
        ButterKnife.bind(this);

        appClass = (ApplicationClass) getApplication();
        pref = pref.getInstance(this.getApplicationContext());

        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

    }

    @OnClick (R.id.btn_exit)
    void exitClick() {
        fxPlay(R.raw.btn_touch);
        btn_exit.startAnimation(btn_click);
        //startActivity(new Intent(OpenSourceActivity.this, MainActivity.class));
        OpenSourceActivity.this.finish();
    }


    public void fxPlay(int target) {
        // 효과음 설정이 되어있을 경우 효과음 재생
        if (pref.getValue("setting_fx", true)) {
            fxPlay(R.raw.btn_touch);
            MediaPlayer fxPlayer = new MediaPlayer();
            fxPlayer = MediaPlayer.create(OpenSourceActivity.this, target);
            fxPlayer.start();
            fxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
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

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
