package eu.jpark.ticsong.activitySupport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Jeon on 2016-07-30.
 */
public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context) {
        super(context,"TICSONGDB",null,1);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db = getWritableDatabase();
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE USER (userid TEXT PRIMARY KEY, name TEXT);");
        db.execSQL("CREATE TABLE SCORE (userid TEXT, score INTEGER, userlevel INTEGER, FOREIGN KEY(userid) REFERENCES USER(userid));");
        db.execSQL("CREATE TABLE MUSIC (url TEXT, artist TEXT, title TEXT, mlevel INTEGER, category INTEGER);");
        db.execSQL("CREATE TABLE MYPAGE(userid TEXT, color TEXT, acc TEXT, FOREIGN KEY(userid) REFERENCES USER(userid));");

        Log.e("DBDB", db.toString());

    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(String userId, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("userId",userId);
        contentValues.put("name", name);

        db.insert("USER", null, contentValues);
        db.close();

        return true;
    }

    public Cursor retrieve(String userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM USER WHERE userId="+userId+";";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

}

