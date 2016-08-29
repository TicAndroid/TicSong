package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.SQLiteAccessModule;
import com.ticcorp.ticsong.module.ServerAccessModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JPark on 2016-08-15.
 */
public class ResultActivity extends Activity {

    public final int MAX_LEVEL = 10; // 최대 레벨 설정

    public CustomPreference pref;
    public int userExp, userLevel, nextExp;
    public String userId;

    public ArrayList<Integer> itemArray = new ArrayList<Integer>(); // 아이템 개수 리스트
    // 아티스트 보여주기, 3초 듣기, 정답 1회 증가, 제목 한 글자 보여주기

    ImageView cloudPanel, score_1, score_2, score_3, score_4, score_5,
        star_1, star_2, star_3, star_4, star_5;
    LinearLayout resultPanel;

    Animation ani_cloud2;
    Animation panel;

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
        /*pref.remove("score");
        for(int i = 1; i <= 5; i++) {
            pref.remove("correct" + i);
        }*/
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
        ResultActivity.this.finish();
    }

    @OnClick(R.id.btn_restart)
    void restartClick() {
        /*pref.remove("score");
        for(int i = 1; i <= 5; i++) {
            pref.remove("correct" + i);
        }*/
        startActivity(new Intent(ResultActivity.this, GameActivity.class));
        ResultActivity.this.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        ani_cloud2 = AnimationUtils.loadAnimation(this, R.anim.translate_cloud2);
        panel = AnimationUtils.loadAnimation(this, R.anim.alpha_anim_result);

        cloudPanel = (ImageView) findViewById(R.id.cloud_panel);
        resultPanel = (LinearLayout) findViewById(R.id.result_panel);

        score_1 = (ImageView) findViewById(R.id.score_1);
        score_2 = (ImageView) findViewById(R.id.score_2);
        score_3 = (ImageView) findViewById(R.id.score_3);
        score_4 = (ImageView) findViewById(R.id.score_4);
        score_5 = (ImageView) findViewById(R.id.score_5);
        star_1 = (ImageView) findViewById(R.id.star_1);
        star_2 = (ImageView) findViewById(R.id.star_2);
        star_3 = (ImageView) findViewById(R.id.star_3);
        star_4 = (ImageView) findViewById(R.id.star_4);
        star_5 = (ImageView) findViewById(R.id.star_5);

        cloudPanel.startAnimation(ani_cloud2);
        resultPanel.startAnimation(panel);
        setResult(); // 게임 결과 처리하기
    }

    public void setResult() {
        pref = pref.getInstance(this.getApplicationContext());
        userId = pref.getValue("userId", "userId");

        DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1 );
        Cursor cursor = null;
        /*
        // 이름 가져오기
        cursor = db.retrieve(StaticSQLite.retrieveUserSQL(pref.getValue("userId", "userId")));
        while(cursor.moveToNext()) {
            user_name = cursor.getString(1);
        }
        */
        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(userId));
        while(cursor.moveToNext()) {
            userExp = cursor.getInt(1);
            userLevel = cursor.getInt(2);
        }

        cursor = db.retrieve(StaticSQLite.retrieveItemSQL(userId));

        while(cursor.moveToNext()) {
            itemArray.add(0, cursor.getInt(1));
            itemArray.add(1, cursor.getInt(2));
            itemArray.add(2, cursor.getInt(3));
            itemArray.add(3, cursor.getInt(4));
        }
        cursor.close();
        db.close();

        userExp = userExp + pref.getValue("score", 0);

        setLevel();

        ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                itemArray.get(0), itemArray.get(1), itemArray.get(2), itemArray.get(3));
        SQLiteAccessModule.getInstance(ResultActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                itemArray.get(0), itemArray.get(1), itemArray.get(2), itemArray.get(3));

        setImage();

        score.setText("You've got "+pref.getValue("score", 0) + " score !");
        exp.setText("exp " + userExp);
        level.setText("Lv." + userLevel);
    }

    public void setLevel() { // 경험치 보고 레벨 업 처리
        nextExp = getResources().getInteger(getResources().getIdentifier("lv" + userLevel, "integer", ResultActivity.this.getPackageName()));
        while(userExp >= nextExp) {
            userExp -= nextExp;
            userLevel++;
            switch ((int) Math.random() * 4) {
                case 0 :
                    itemArray.set(0, itemArray.get(0) + 1);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n아티스트 보여주기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 1 :
                    itemArray.set(1, itemArray.get(1) + 1);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n3초 듣기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    itemArray.set(2, itemArray.get(2) + 1);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n정답 1회 증가 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 3 :
                    itemArray.set(3, itemArray.get(3) + 1);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n제목 한 글자 보여주기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    }

    public void setImage() { // 결과 이미지 변경
        // for문으로 간편화하고 싶으나 resource를 순차적으로 불러오는 방법을 찾아야 해서 우선은 나열해둠
        switch (pref.getValue("correct1", 0)) {
            case 3:
                star_1.setBackgroundResource(R.drawable.star);
                score_1.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                star_1.setBackgroundResource(R.drawable.star_60);
                score_1.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                star_1.setBackgroundResource(R.drawable.star_30);
                score_1.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                star_1.setBackgroundResource(R.drawable.star_0);
                score_1.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct2", 0)) {
            case 3:
                star_2.setBackgroundResource(R.drawable.star);
                score_2.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                star_2.setBackgroundResource(R.drawable.star_60);
                score_2.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                star_2.setBackgroundResource(R.drawable.star_30);
                score_2.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                star_2.setBackgroundResource(R.drawable.star_0);
                score_2.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct3", 0)) {
            case 3:
                star_3.setBackgroundResource(R.drawable.star);
                score_3.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                star_3.setBackgroundResource(R.drawable.star_60);
                score_3.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                star_3.setBackgroundResource(R.drawable.star_30);
                score_3.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                star_3.setBackgroundResource(R.drawable.star_0);
                score_3.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct4", 0)) {
            case 3:
                star_4.setBackgroundResource(R.drawable.star);
                score_4.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                star_4.setBackgroundResource(R.drawable.star_60);
                score_4.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                star_4.setBackgroundResource(R.drawable.star_30);
                score_4.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                star_4.setBackgroundResource(R.drawable.star_0);
                score_4.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct5", 0)) {
            case 3:
                star_5.setBackgroundResource(R.drawable.star);
                score_5.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                star_5.setBackgroundResource(R.drawable.star_60);
                score_5.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                star_5.setBackgroundResource(R.drawable.star_30);
                score_5.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                star_5.setBackgroundResource(R.drawable.star_0);
                score_5.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
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
