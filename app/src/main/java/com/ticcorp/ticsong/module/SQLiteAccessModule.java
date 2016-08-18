package com.ticcorp.ticsong.module;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ticcorp.ticsong.DTO.LoginView;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;

/**
 * Created by Daesub Kim on 2016-08-18.
 */
public class SQLiteAccessModule {

    private static SQLiteAccessModule instance;
    static Context mContext;
    static {
        instance = new SQLiteAccessModule();
    }

    public static SQLiteAccessModule getInstance(Context context) {
        mContext = context;
        if(instance !=null)
            return instance;
        else
            return new SQLiteAccessModule();
    }
    private SQLiteAccessModule() {}

    /**
     * 기존의 Table을 모두 Drop 후, Create. 서버에서 가져온 값을 insert 한다.
     * @param loginView
     */
    public void login(LoginView loginView) {
        /* DB Insert */
        DBManager db = new DBManager(mContext, StaticSQLite.TICSONG_DB, null, 1 );
        db.drop();
        db.create();

        db.insert(StaticSQLite.insertUserSQL(loginView.getUserId(), loginView.getName(), loginView.getPlatform()));
        db.insert(StaticSQLite.insertMyScoreSQL(loginView.getUserId(), loginView.getExp(), loginView.getUserLevel()));
        db.insert(StaticSQLite.insertItemSQL(loginView.getUserId(), loginView.getItem1Cnt(), loginView.getItem2Cnt()
                , loginView.getItem3Cnt(), loginView.getItem4Cnt()));

        db.close();
    }

    public void logout(String userId) {

        int exp=0, userLevel=0, item1Cnt=0, item2Cnt=0, item3Cnt=0, item4Cnt=0;
        DBManager db = new DBManager(mContext, StaticSQLite.TICSONG_DB, null, 1 );
        Cursor cursor = null;
        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(userId));
        while(cursor.moveToNext()) {
            exp = cursor.getInt(1);
            userLevel = cursor.getInt(2);
            Log.e("MyScore SQLite조회", userId + exp + userLevel );
        }

        cursor = db.retrieve(StaticSQLite.retrieveItemSQL(userId));
        while(cursor.moveToNext()) {
            item1Cnt = cursor.getInt(1);
            item2Cnt = cursor.getInt(2);
            item3Cnt = cursor.getInt(3);
            item4Cnt = cursor.getInt(4);
            Log.e("ITEM SQLite조회", userId + item1Cnt + item2Cnt + item3Cnt + item4Cnt );
        }

        ServerAccessModule.getInstance().logout(userId, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt );

        db.drop();
        cursor.close();
        db.close();

    }
    /**
     * 5곡 게임이 끝난 후, 호출. SQLite Update. CustomPreference 에서 꺼내서 argu에 넣어준다.
     * @param userId
     * @param exp
     * @param userLevel
     * @param item1Cnt
     * @param item2Cnt
     * @param item3Cnt
     * @param item4Cnt
     */
    public void gameFinished (String userId, int exp, int userLevel, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        /* SQLite DB Update */
        DBManager db = new DBManager(mContext, StaticSQLite.TICSONG_DB, null, 1 );
        db.update(StaticSQLite.updateMyScoreSQL(userId, exp, userLevel));
        db.update(StaticSQLite.updateItemSQL(userId, item1Cnt, item2Cnt
                , item3Cnt, item4Cnt));
        db.close();
    }


}
