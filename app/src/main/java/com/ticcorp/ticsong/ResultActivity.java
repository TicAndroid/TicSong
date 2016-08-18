package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-08-15.
 */
public class ResultActivity extends Activity {

    public CustomPreference pref;
    public int now_exp;

    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.exp)
    TextView exp;
    @Bind(R.id.level)
    TextView level;

    @OnClick(R.id.btn_share)
    void shareClick() {
        takeScreenshot();
    }

    @OnClick(R.id.btn_main)
    void mainClick() {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        ResultActivity.this.finish();
    }

    @OnClick(R.id.btn_restart)
    void restartClick() {
        startActivity(new Intent(ResultActivity.this, GameActivity.class));
        ResultActivity.this.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        getResult(); // 게임 결과 받아오기
        sendResult(); // 게임 결과 DB 전송
    }

    public void getResult() {
        pref = pref.getInstance(this.getApplicationContext());

        now_exp = pref.getValue("exp", 0) + pref.getValue("score", 0);
        pref.put("exp", now_exp);

        score.setText(pref.getValue("score", 0) + "점 획득");
        exp.setText("exp " + pref.getValue("exp", 0));
        level.setText("Lv." + pref.getValue("userLevel", 1));
    }

    public void sendResult() {

    }

    public void takeScreenshot() {

    }
}
