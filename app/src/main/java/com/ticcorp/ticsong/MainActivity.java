package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.view.*;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ticcorp.ticsong.activitySupport.CustomBitmapPool;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.ticcorp.ticsong.utils.BackPressCloseHandler;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;


public class MainActivity extends Activity {

    // 테스트를 위한 계정 데이터 변수 부분
    public String user_name;
    public int user_id, user_lv, user_exp, next_exp, now_exp, required_exp;
    public ArrayList<Integer> user_itemArray = new ArrayList<Integer>();

    private BackPressCloseHandler backPressCloseHandler;

    @Bind(R.id.btn_play)
    ImageButton mainJukeBox;
    @Bind(R.id.btn_ranking)
    ImageButton btn_ranking;
    @Bind(R.id.btn_setting)
    ImageButton btn_setting;
    @Bind(R.id.juke_bold)
    ImageView img_juke_bold;
    @Bind(R.id.juke_sport)
    ImageView img_juke_sport;
    @Bind(R.id.item1_cnt)
    TextView item1_cnt;
    @Bind(R.id.item2_cnt)
    TextView item2_cnt;
    @Bind(R.id.item3_cnt)
    TextView item3_cnt;
    @Bind(R.id.item4_cnt)
    TextView item4_cnt;
    @Bind(R.id.profile_progressbar)
    ProgressBar exp_bar;

    Animation button_anim, background_anim, juke_bold, juke_sport;
    @Bind(R.id.profile_img)
    ImageView profile_img;

    Bitmap mPicBitmap;

    TextView profile_id, profile_level;
    ProgressBar profile_progressbar;

    CustomPreference pref;

    //페이스북

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setUserData();

        backPressCloseHandler = new BackPressCloseHandler(this);

        background_anim = AnimationUtils.loadAnimation(this,R.anim.base_rotate_anim);
        button_anim = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);
        juke_bold = AnimationUtils.loadAnimation(this, R.anim.scale_juke);
        juke_sport = AnimationUtils.loadAnimation(this, R.anim.alpha_juke);

        img_juke_bold.startAnimation(juke_bold);
        img_juke_sport.startAnimation(juke_sport);



        ImageView star_background = (ImageView) findViewById(R.id.background_star);
        star_background.startAnimation(background_anim);

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), SettingActivity.class));
            }
        });

/*
        Button btn_friend = (Button) findViewById(R.id.btn_friend);

        btn_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            // handle the result
                                Log.i("ticlog", "MainActivity" + response.toString());
                            }
                        }
                ).executeAsync();
            }
        });
/*
        /*final DBHelper dbHelper = new DBHelper(getApplicationContext(), "MoneyBook.db", null, 1);

        // 테이블에 있는 모든 데이터 출력
        final TextView result = (TextView) findViewById(R.id.result);

        final EditText etDate = (EditText) findViewById(R.id.date);
        final EditText etItem = (EditText) findViewById(R.id.item);
        final EditText etPrice = (EditText) findViewById(R.id.price);

        // 날짜는 현재 날짜로 고정
        // 현재 시간 구하기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 출력될 포맷 설정
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        etDate.setText(simpleDateFormat.format(date));

        // DB에 데이터 추가
        Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDate.getText().toString();
                String item = etItem.getText().toString();
                int price = Integer.parseInt(etPrice.getText().toString());

                dbHelper.insert(date, item, price);
                result.setText(dbHelper.getResult());
            }
        });

        // DB에 있는 데이터 수정
        Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();
                int price = Integer.parseInt(etPrice.getText().toString());

                dbHelper.update(item, price);
                result.setText(dbHelper.getResult());
            }
        });

        // DB에 있는 데이터 삭제
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = etItem.getText().toString();

                dbHelper.delete(item);
                result.setText(dbHelper.getResult());
            }
        });

        // DB에 있는 데이터 조회
        Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText(dbHelper.getResult());
            }
        });*/

    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void setUserData () { // DB 연결
        profile_id = (TextView) findViewById(R.id.profile_id);
        profile_level = (TextView) findViewById(R.id.profile_level);
        profile_progressbar = (ProgressBar) findViewById(R.id.profile_progressbar);

        pref = pref.getInstance(this.getApplicationContext());

        pref.put("platform", 2); // 약관 동의 후 튜토리얼까지 봄 확인(임시로 platform에 대입)


        //강제 레벨/경험치 주입
        /*
        ServerAccessModule.getInstance().gameFinished(pref.getValue("userId", "userId"), 22, 2, 3, 3, 3, 3);
        SQLiteAccessModule.getInstance(MainActivity.this.getApplicationContext()).gameFinished(pref.getValue("userId", "userId"), 22, 2, 3, 3, 3, 3);
        */

        /*DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1 );
        Cursor cursor = null;
        cursor = db.retrieve(StaticSQLite.retrieveUserSQL(pref.getValue("userId", "userId")));
        while(cursor.moveToNext()) {
            user_name = cursor.getString(1);
        }

        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(pref.getValue("userId", "userId")));
        while(cursor.moveToNext()) {
            user_exp = cursor.getInt(1);
            user_lv = cursor.getInt(2);
        }
        cursor.close();
        db.close();*/

        user_name = pref.getValue("name", "name");
        user_lv = pref.getValue("userLevel", 1);
        user_exp = pref.getValue("exp", 0);

        profile_id.setText(user_name + " ");
        profile_level.setText(user_lv + " ");

        next_exp = 0;
        for(int i = 1; i <= user_lv; i++) { // 누적 경험치를 구하는 함수
            if (i == user_lv) {
                // 경험치바에 표시할 현재 경험치는 현재 누적 경험치에서 이전 레벨까지의 누적 경험치를 뺀 수치
                now_exp = user_exp - next_exp;
            }
            next_exp += getResources().getInteger(getResources().getIdentifier("lv" + i, "integer", MainActivity.this.getPackageName()));
            Log.i("ticlog Main", "next_exp 계산중 : " + next_exp + "/" + i);
        }
        required_exp = getResources().getInteger(getResources().getIdentifier("lv" + user_lv, "integer", MainActivity.this.getPackageName()));
        // required_exp는 현재 레벨에서 다음 레벨로 가기 위한 요구 경험치, next_exp는 다음 레벨로 가기 위한 총 누적 경험치

        profile_progressbar.setMax(required_exp);
        profile_progressbar.setProgress(now_exp);

        item1_cnt.setText(pref.getValue("item1Cnt", 0) + " ");
        item2_cnt.setText(pref.getValue("item2Cnt", 0) + "");
        item3_cnt.setText(pref.getValue("item3Cnt", 0) + "");
        item4_cnt.setText(pref.getValue("item4Cnt", 0) + "");

        Glide.with(this).load("http://graph.facebook.com/" +
                pref.getValue("userId", "userId") + "/picture?type=large").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).
                error(R.drawable.profile_main_image).into(profile_img);

        Log.i("ticlog Main", user_name + " / Lv." + user_lv + " / " + now_exp + "(" + user_exp + ")/ " + required_exp + "(" + next_exp + ")");
    }

    /*public static Bitmap getBitmapFromURL (String target) {
        // 이미지 URL에서 불러오는 함수
        try {
            URL url = new URL(target);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap picBitmap = BitmapFactory.decodeStream(is);
            Log.i("ticlog", "Img load success");
            return picBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ticlog", "Img load fail");
            return null;
        }
    }*/

    /*public static Bitmap getBitmapFromURL (String target) {
        try {
            URL url = new URL(target);
            Bitmap picBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.i("ticlog", "Img load success");
            return picBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("ticlog", "Img load fail");
            return null;
        }
    } */

    // JukeBox 이미지 클릭시 GameActivity로 이동
    @OnClick(R.id.btn_play)
    void mainJokeBoxClicked() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.btn_ranking)
    void rankingClicked() {
        startActivity(new Intent(getApplication(), RankingActivity.class));
    }


}