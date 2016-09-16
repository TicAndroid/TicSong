package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-09-05.
 */
public class OpenSourceActivity extends Activity {

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;

    public Animation btn_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opensource);
        ButterKnife.bind(this);

        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

    }

    @OnClick (R.id.btn_exit)
    void exitClick() {
        btn_exit.startAnimation(btn_click);
        startActivity(new Intent(OpenSourceActivity.this, MainActivity.class));
        OpenSourceActivity.this.finish();
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
