package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-08-15.
 */
public class ResultActivity extends Activity {

    public CustomPreference pref;
    public int now_exp;
    public String address;

    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.exp)
    TextView exp;
    @Bind(R.id.level)
    TextView level;

    @OnClick(R.id.btn_share)
    void shareClick() {
        shareScreenshot();
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
    public void onCreate(Bundle savedInstanceState) {
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

    public void shareScreenshot() {
        // 캡쳐하기
        String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data";
        File dir = new File(absolutePath, "com.ticcorp.ticsong");
        if(!dir.exists()) { // 폴더가 없을 경우 폴더 생성
            dir.mkdir();
        }
        File capture = new File(dir, "capture.jpeg"); // 파일 위치 설정

        View containter = getWindow().getDecorView();
        containter.buildDrawingCache();
        Bitmap captureView = containter.getDrawingCache();

        FileOutputStream fostream;
        try {
            fostream = new FileOutputStream(capture);
            captureView.compress(Bitmap.CompressFormat.JPEG, 100, fostream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 공유하기
        Uri uri = Uri.fromFile(new File(capture + ""));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "공유하기"));
    }

}
