package com.ticcorp.ticsong.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Daesub Kim on 2016-08-15.
 */
public class DBManager extends SQLiteOpenHelper {

    final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS USER ( userid TEXT, name TEXT, platform INTEGER);";
    final String CREATE_TABLE_MYSCORE = "CREATE TABLE IF NOT EXISTS MYSCORE ( userid TEXT, exp INTEGER, userlevel INTEGER);";
    final String CREATE_TABLE_ITEM = "CREATE TABLE IF NOT EXISTS ITEM ( userid TEXT, item1Cnt INTEGER, item2Cnt INTEGER, item3Cnt INTEGER, item4Cnt INTEGER);";

    final String DROP_TABLE_USER = "DROP TABLE IF EXISTS USER;";
    final String DROP_TABLE_MYSCORE = "DROP TABLE IF EXISTS MYSCORE;";
    final String DROP_TABLE_ITEM = "DROP TABLE IF EXISTS ITEM;";

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MYSCORE);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void create() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_MYSCORE);
        db.execSQL(CREATE_TABLE_ITEM);
    }
    public void drop() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(DROP_TABLE_USER);
        db.execSQL(DROP_TABLE_MYSCORE);
        db.execSQL(DROP_TABLE_ITEM);
    }

    public void insert(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        //db.close();
    }

    public void update(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        //db.close();
    }

    public void delete(String _query) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        //db.close();
    }



    public Cursor retrieve(String _query) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(_query, null);
        //"select * from USER"
        /*while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " / name : "
                    + cursor.getString(1)
                    + ", platform : "
                    + cursor.getInt(2)
                    + "\n";
        }*/
        //db.close();

        return cursor;
    }

}
