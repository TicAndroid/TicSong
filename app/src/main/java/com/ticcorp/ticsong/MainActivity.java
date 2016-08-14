package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class MainActivity extends Activity {

    // 테스트를 위한 계정 데이터 변수 부분, 나중에 로그인 페이지로 옮길 것
    public String user_name;
    public int user_lv, user_exp;
    public ArrayList<Integer> user_itemArray = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUserData();

        ImageButton btn_start = (ImageButton) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), GameActivity.class));
            }
        });


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

    public void setUserData () { // DB 연결 테스트
        user_name = "틱송님";
        user_lv = 3;
        user_exp = 1500;
        user_itemArray.add(0, 1); // 아티스트 보여주기 아이템 개수
        user_itemArray.add(1, 1); // 3초 듣기 아이템 개수
        user_itemArray.add(2, 2); // 정답 1회 증가 아이템 개수
        user_itemArray.add(3, 1); // 제목 한 글자 보여주기 아이템 개수
    }


}