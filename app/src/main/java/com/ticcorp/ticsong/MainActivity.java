package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
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

    ApplicationClass appClass;

    // 테스트를 위한 계정 데이터 변수 부분
    public String user_name;
    public int user_id, user_lv, user_exp, next_exp, now_exp, required_exp;
    public final int MAX_LEVEL = 99;
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
    @Bind(R.id.item1)
    ImageButton item1;
    @Bind(R.id.item2)
    ImageButton item2;
    @Bind(R.id.item3)
    ImageButton item3;
    @Bind(R.id.item4)
    ImageButton item4;
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

        appClass = (ApplicationClass) getApplication();

        setUserData();

        backPressCloseHandler = new BackPressCloseHandler(this);

        background_anim = AnimationUtils.loadAnimation(this,R.anim.base_rotate_anim);
        button_anim = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);
        juke_bold = AnimationUtils.loadAnimation(this, R.anim.scale_juke);
        juke_sport = AnimationUtils.loadAnimation(this, R.anim.alpha_juke);

        img_juke_bold.startAnimation(juke_bold);
        img_juke_sport.startAnimation(juke_sport);

        Log.v("test"," "+pref.getValue("tutorial",-1));

        ImageView star_background = (ImageView) findViewById(R.id.background_star);
        star_background.startAnimation(background_anim);


        //세팅 버튼
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fxPlay(R.raw.btn_touch);
                startActivity(new Intent(getApplication(), SettingActivity.class));
            }
        });

        appClass.bgmPlay(R.raw.jellyfish_in_space);

        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "가수 이름 보여주기 아이템", Toast.LENGTH_SHORT).show();
            }
        });


        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "생명력 증가 아이템", Toast.LENGTH_SHORT).show();
            }
        });

        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "한 글자 보여주기 아이템", Toast.LENGTH_SHORT).show();
            }
        });

        item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "노래 3초 듣기 아이템", Toast.LENGTH_SHORT).show();
            }
        });


    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    public void setUserData () { // DB 연결
        profile_id = (TextView) findViewById(R.id.profile_id);
        profile_level = (TextView) findViewById(R.id.profile_level);
        profile_progressbar = (ProgressBar) findViewById(R.id.profile_progressbar);

        pref = pref.getInstance(this.getApplicationContext());

        pref.put("tutorial", 2); // 약관 동의 후 튜토리얼까지 봄 확인(임시로 프리퍼런스에 대입)


        user_name = pref.getValue("name", "name");
        user_lv = pref.getValue("userLevel", 1);
        user_exp = pref.getValue("exp", 0);

        profile_id.setText(user_name + " ");
        profile_level.setText(user_lv + " ");

        if (user_lv < MAX_LEVEL) { // 최대 레벨 이하일 때
            next_exp = 0;
            for (int i = 1; i <= user_lv; i++) { // 누적 경험치를 구하는 함수
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

            //Log.i("ticlog Main", user_name + " / Lv." + user_lv + " / " + now_exp + "(" + user_exp + ")/ " + required_exp + "(" + next_exp + ")");
        } else { // 최대 레벨일 때
            profile_progressbar.setMax(100);
            profile_progressbar.setProgress(100);
        }
        item1_cnt.setText(pref.getValue("item1Cnt", 0) + " ");
        item2_cnt.setText(pref.getValue("item2Cnt", 0) + "");
        item3_cnt.setText(pref.getValue("item3Cnt", 0) + "");
        item4_cnt.setText(pref.getValue("item4Cnt", 0) + "");

/*
        Glide.with(this).load("http://graph.facebook.com/" +
                pref.getValue("userId", "userId") + "/picture?type=large").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).
                error(R.drawable.profile_main_image).into(profile_img);
*/
        Glide.with(this).load("http://mud-kage.kakao.co.kr/14/dn/btqdhBA2f1Q/nvBaVQakk7MfFkw79Cu1UK/o.jpg").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).
                error(R.drawable.profile_main_image).into(profile_img);

    }

    public void fxPlay(int target) {
        // 효과음 설정이 되어있을 경우 효과음 재생
        if (pref.getValue("setting_fx", true)) {
            MediaPlayer fxPlayer = new MediaPlayer();
            fxPlayer = MediaPlayer.create(MainActivity.this, target);
            fxPlayer.start();
            fxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        }
    }

    // JukeBox 이미지 클릭시 GameActivity로 이동
    @OnClick(R.id.btn_play)
    void mainJokeBoxClicked() {
        fxPlay(R.raw.btn_touch);
        Intent intent = new Intent(this, GameActivity.class);
        appClass.bgmStop();
        startActivity(intent);
        this.finish();
    }

    @OnClick(R.id.btn_ranking)
    void rankingClicked() {
        fxPlay(R.raw.btn_touch);
        startActivity(new Intent(getApplication(), RankingActivity.class));
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

}