package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JPark on 2016-09-05.
 */
public class InfoActivity extends Activity {

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;

    public Animation btn_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);

        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

    }

    @OnClick (R.id.btn_exit)
    void exitClick() {
        btn_exit.startAnimation(btn_click);
        startActivity(new Intent(InfoActivity.this, MainActivity.class));
        InfoActivity.this.finish();
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
