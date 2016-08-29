package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.*;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // 테스트를 위한 계정 데이터 변수 부분
    public String user_name;
    public int user_id, user_lv, user_exp, next_exp;
    public ArrayList<Integer> user_itemArray = new ArrayList<Integer>();

    Animation button_anim;

    TextView profile_id, profile_level;
    ProgressBar profile_progressbar;

    CustomPreference pref;

    //페이스북

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUserData();

        button_anim = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);
        final ImageButton btn_start = (ImageButton) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.startAnimation(button_anim);
                startActivity(new Intent(getApplication(), GameActivity.class));
                //MainActivity.this.finish();
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

    public void setUserData () { // DB 연결
        profile_id = (TextView) findViewById(R.id.profile_id);
        profile_level = (TextView) findViewById(R.id.profile_level);
        profile_progressbar = (ProgressBar) findViewById(R.id.profile_progressbar);

        pref = pref.getInstance(this.getApplicationContext());
        DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1 );
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
        db.close();

        profile_id.setText(user_name);
        profile_level.setText(user_lv + "");

        next_exp = getResources().getInteger(getResources().getIdentifier("lv" + user_lv, "integer", MainActivity.this.getPackageName()));

        Log.i("ticlog Main", user_lv + "/" + next_exp);

        profile_progressbar.setMax(next_exp);
        profile_progressbar.setProgress(user_exp);

        /*

        user_name = pref.getValue("name", "-");
        user_lv = 3;
        user_exp = 1500;
        user_itemArray.add(0, 1); // 아티스트 보여주기 아이템 개수
        user_itemArray.add(1, 1); // 3초 듣기 아이템 개수
        user_itemArray.add(2, 2); // 정답 1회 증가 아이템 개수
        user_itemArray.add(3, 1); // 제목 한 글자 보여주기 아이템 개수 */
    }


}