package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ticcorp.ticsong.activitySupport.CustomBitmapPool;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.SQLiteAccessModule;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by JPark on 2016-08-15.
 */
public class ResultActivity extends Activity {

    public final int MAX_LEVEL = 10; // 최대 레벨 설정

    public CustomPreference pref;
    public int userExp, userLevel, nextExp, nowExp, requiredExp;
    public String userId;

    //public ArrayList<Integer> itemArray = new ArrayList<Integer>(); // 아이템 개수 리스트
    // 아티스트 보여주기, 3초 듣기, 정답 1회 증가, 제목 한 글자 보여주기

    ImageView score_1, score_2, score_3, score_4, score_5;

    ImageView boom, item,lvl_panel,profile_img;

    RelativeLayout item_gift;
    ProgressBar profile_progressbar;

    @Bind(R.id.score)
    TextView score;
    /*
    @Bind(R.id.exp)
    TextView exp;
    */
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        score_1 = (ImageView) findViewById(R.id.score_1);
        score_2 = (ImageView) findViewById(R.id.score_2);
        score_3 = (ImageView) findViewById(R.id.score_3);
        score_4 = (ImageView) findViewById(R.id.score_4);
        score_5 = (ImageView) findViewById(R.id.score_5);

        lvl_panel = (ImageView) findViewById(R.id.lvl_panel);
        item_gift = (RelativeLayout) findViewById(R.id.item_gift);
        boom = (ImageView) findViewById(R.id.boom);
        item = (ImageView) findViewById(R.id.item2);

        profile_img = (ImageView) findViewById(R.id.profile_img);

        profile_progressbar = (ProgressBar) findViewById(R.id.profile_progressbar);

        pref = pref.getInstance(this.getApplicationContext());

        setResult(); // 게임 결과 처리하기

        Glide.with(this).load("http://graph.facebook.com/" +
                userId + "/picture?type=large").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).
                error(R.drawable.profile_main_image).into(profile_img);
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void setResult() {
        userId = pref.getValue("userId", "userId");

        /*
        DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1 );
        Cursor cursor = null;

        // 이름 가져오기
        cursor = db.retrieve(StaticSQLite.retrieveUserSQL(pref.getValue("userId", "userId")));
        while(cursor.moveToNext()) {
            user_name = cursor.getString(1);
        }

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
        */

        userExp = pref.getValue("exp", 0);
        userLevel = pref.getValue("userLevel", 1);
        //경험치 자동 200씩 추가
        //userExp = userExp + pref.getValue("score", 0) + 200;
        userExp = userExp + pref.getValue("score", 0);

        setLevel();

        ServerAccessModule.getInstance().gameFinished(userId, userExp, userLevel,
                pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
        SQLiteAccessModule.getInstance(ResultActivity.this.getApplicationContext()).gameFinished(userId, userExp, userLevel,
                pref.getValue("item1Cnt", 0), pref.getValue("item2Cnt", 0),
                pref.getValue("item3Cnt", 0), pref.getValue("item4Cnt", 0));
        pref.put("exp", userExp);
        pref.put("userLevel", userLevel);

        setImage();

        score.setText(pref.getValue("score", 0) + " ");
        //exp.setText("exp " + userExp);
        level.setText(userLevel + " ");


        nextExp = 0;
        for(int i = 1; i <= userLevel; i++) { // 누적 경험치를 구하는 함수
            if (i == userLevel) {
                // 경험치바에 표시할 현재 경험치는 현재 누적 경험치에서 이전 레벨까지의 누적 경험치를 뺀 수치
                nowExp = userExp - nextExp;
            }
            nextExp += getResources().getInteger(getResources().getIdentifier("lv" + i, "integer", ResultActivity.this.getPackageName()));
            Log.i("ticlog Main", "next_exp 계산중 : " + nextExp + "/" + i);
        }
        requiredExp = getResources().getInteger(getResources().getIdentifier("lv" + userLevel, "integer", ResultActivity.this.getPackageName()));
        // requiredExp는 현재 레벨에서 다음 레벨로 가기 위한 요구 경험치, nextExp는 다음 레벨로 가기 위한 총 누적 경험치(setLevel에서 사용된 변수 초기화 후 사용)

        profile_progressbar.setMax(requiredExp);
        profile_progressbar.setProgress(nowExp);
    }

    public void setLevel() { // 경험치 보고 레벨 업 처리
        //nextExp = getResources().getInteger(getResources().getIdentifier("lv" + userLevel, "integer", ResultActivity.this.getPackageName()));
        nextExp = 0;
        for(int i = 1; i <= userLevel; i++) {
            nextExp += getResources().getInteger(getResources().getIdentifier("lv" + i, "integer", ResultActivity.this.getPackageName()));
        }
        while(userExp >= nextExp) {
            //userExp는 누적 경험치이므로 경험치를 초기화하지 않음
            //userExp -= nextExp;
            userLevel++;
            lvl_panel.setVisibility(View.VISIBLE);
            item_gift.setVisibility(View.VISIBLE);
            boom.setVisibility(View.INVISIBLE);

            switch ((int) Math.random() * 4) {
                case 0 :
                    pref.put("item1Cnt", pref.getValue("item1Cnt", 0) + 1);
                    item.setBackgroundResource(R.drawable.item_artist);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n아티스트 보여주기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 1 :
                    pref.put("item2Cnt", pref.getValue("item2Cnt", 0) + 1);
                    item.setBackgroundResource(R.drawable.item_thirdsecond);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n3초 듣기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 2 :
                    pref.put("item3Cnt", pref.getValue("item3Cnt", 0) + 1);
                    item.setBackgroundResource(R.drawable.item_onemore);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n정답 1회 증가 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                case 3 :
                    pref.put("item4Cnt", pref.getValue("item4Cnt", 0) + 1);
                    item.setBackgroundResource(R.drawable.item_onechar);
                    Toast.makeText(ResultActivity.this, "레벨 업!\n제목 한 글자 보여주기 아이템 1개 획득!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            nextExp = 0;
            for(int i = 1; i <= userLevel; i++) {
                nextExp += getResources().getInteger(getResources().getIdentifier("lv" + i, "integer", ResultActivity.this.getPackageName()));
            }
        }

    }

    public void setImage() { // 결과 이미지 변경
        // for문으로 간편화하고 싶으나 resource를 순차적으로 불러오는 방법을 찾아야 해서 우선은 나열해둠
        switch (pref.getValue("correct1", 0)) {
            case 3:
                score_1.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                score_1.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                score_1.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                score_1.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct2", 0)) {
            case 3:
                score_2.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                score_2.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                score_2.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                score_2.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct3", 0)) {
            case 3:
                score_3.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                score_3.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                score_3.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                score_3.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct4", 0)) {
            case 3:
                score_4.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                score_4.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                score_4.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
                score_4.setBackgroundResource(R.drawable.score_0);
                break;
            default:
                break;
        }
        switch (pref.getValue("correct5", 0)) {
            case 3:
                score_5.setBackgroundResource(R.drawable.score_100);
                break;
            case 2:
                score_5.setBackgroundResource(R.drawable.score_60);
                break;
            case 1:
                score_5.setBackgroundResource(R.drawable.score_30);
                break;
            case 0:
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
