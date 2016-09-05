package com.ticcorp.ticsong;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ticcorp.ticsong.core.ItemController;
import com.ticcorp.ticsong.core.LoginController;
import com.ticcorp.ticsong.core.MyScoreController;
import com.ticcorp.ticsong.core.RegisterController;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.SQLiteAccessModule;
import com.ticcorp.ticsong.module.ServerAccessModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daesub Kim on 2016-08-09.
 */
public class ServerTestActivity extends Activity {

    // 로그인 버튼
    @Bind(R.id.btn_login)
    Button loginBtn;

    // 로그아웃 버튼 : 반드시 로그인 버튼을 먼저 클릭하고 누를 것.
    @Bind(R.id.btn_logout)
    Button logoutBtn;

    // SQLite에서 MyScore, Item 조회 버튼 : 반드시 로그인 버튼을 먼저 클릭하고 누를 것.
    @Bind(R.id.btn_select)
    Button selectBtn;

    // GameOver 버튼 : 반드시 로그인 버튼을 먼저 클릭하고 누를 것.
    @Bind(R.id.btn_gameover)
    Button gameoverBtn;

    //  Top20 랭커 조회 버튼 : 반드시 로그인 버튼을 먼저 클릭하고 누를 것.
    @Bind(R.id.btn_get_top_rank)
    Button btnGetTopRank;

    // 친구 정보/점수 조회 버튼 : 반드시 로그인 버튼을 먼저 클릭하고 누를 것.
    @Bind(R.id.btn_friend)
    Button friendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_test);

        ButterKnife.bind(this);
    }

    // 로그인버튼 클릭시
    @OnClick(R.id.btn_login)
    void loginBtn() {

        final String userId = "2222";
        final String name = "틱송유저";
        final int platform = 1;

        /* 호출 메소드 ( 주의사항 : deleteUser 메소드는 레트로핏 동기 통신이기때문에,
        *            Worker Thread 에서 호출해야 함 !!! )
        *            */
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerAccessModule.getInstance().login(getApplicationContext(), userId, name, platform);
            }
        }).start();

        // Argu : 소셜로그인 후 (userId, name, platform) 값을 매개변수로 넣어준다.
        // 기능 : 서버 다녀와서, / SQLite DB Table Drop, Create, Insert, CustomPreference에 Put
        /*
        CustomPreference 형식 :
            (LoginView loginView)
	        customPreference.put("userId",loginView.getUserId());
            customPreference.put("name",loginView.getName());
            customPreference.put("platform",loginView.getPlatform());
            customPreference.put("exp",loginView.getExp());
            customPreference.put("userLevel",loginView.getUserLevel());
            customPreference.put("item1Cnt",loginView.getItem1Cnt());
            customPreference.put("item2Cnt",loginView.getItem2Cnt());
            customPreference.put("item3Cnt",loginView.getItem3Cnt());
            customPreference.put("item4Cnt",loginView.getItem4Cnt());

            LoginController에서
            userId , name, platform, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt
            모든 값을 preference에 넣어준다.
        */

    }

    // 로그아웃 클릭시
    @OnClick(R.id.btn_logout)
    void logoutBtn() {

        //String userId = "5555";
        CustomPreference pref = CustomPreference.getInstance(getApplicationContext());

        /* 호출 메소드 */
        //SQLiteAccessModule.getInstance(this.getApplicationContext()).logout(pref.getValue("userId", "userId"));
        // Argu : Preference에서 userId 뽑음.
        // 기능 : SQLite DB Table 에서 Retrieve, Drop 후, ServerAccessModule.gameFinished() 호출
    }

    // 5곡 게임 정상종료시
    @OnClick(R.id.btn_gameover)
    void gameoverBtn() {

        CustomPreference customPreference = CustomPreference.getInstance(this.getApplication());
        String userId = customPreference.getValue("userId", "userId");
        int exp = customPreference.getValue("exp", 0);
        int userLevel = customPreference.getValue("userLevel", 0);
        int item1Cnt = customPreference.getValue("item1Cnt", 0);
        int item2Cnt = customPreference.getValue("item2Cnt", 0);
        int item3Cnt = customPreference.getValue("item3Cnt", 0);
        int item4Cnt = customPreference.getValue("item4Cnt", 0);
        // CustomPreference에서 userId, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt를 꺼낸다.

        /* 호출 메소드 */
        ServerAccessModule.getInstance().gameFinished(userId, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
        // Argu : Preference에서 값을 모두 가져옴(get)
        // 기능 : Server에   MyScore, Item 정보를 Update.
        //SQLiteAccessModule.getInstance(this.getApplicationContext()).gameFinished(userId, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
        // Argu : Preference에서 값을 모두 가져옴(get)
        // 기능 : SQLite에   MyScore, Item 정보를 Update.
    }

    // Top20랭커 버튼 클릭시
    @OnClick(R.id.btn_get_top_rank)
    void btnGetTopRankClicked() {

        CustomPreference customPreference = CustomPreference.getInstance(this.getApplication());
        String userId = customPreference.getValue("userId", "userId");

        /* 호출 메소드 */
        ServerAccessModule.getInstance().retrieveTopRanker(userId);
        // Argu : Preference에서 userId 뽑음.
        // 기능 : 서버에서 Top Ranker 데이터 가져옴.
        // 가져온 Top Ranker는  [ ServerAccessModule.TOP_RANKER_LIST ] 에 담겨있음 ( Type : List<ScoreView> )

    }

    // 친구 버튼 클릭시
    @OnClick(R.id.btn_friend)
    void friendBtnClicked() {

        CustomPreference customPreference = CustomPreference.getInstance(this.getApplication());
        String userId = customPreference.getValue("userId", "userId");

        /* 아래의 리스트는 FaceBook 에서 내 친구 리스트 JSON 중에
         친구 Id를 String으로  뽑아서 List<String>에 add() 한다.*/
        List<String> fList = new ArrayList<String>();
        fList.add("1111");
        fList.add("2222");
        fList.add("3333");

        /* 호출 메소드 */
        ServerAccessModule.getInstance().retrieveFriendList(userId, fList);
        // Argu : userId = Preference 뽑음. / friendIdList = FaceBook의 친구Id 리스트를 List<String>에 담아서 보냄.
        // 기능 : 서버에서 친구/목록 데이터 가져옴.
        // 친구 목록/점수는  [ ServerAccessModule.FRIEND_LIST ] 에 담겨있음 ( Type : List<ScoreView> )
    }


    // DELETE USER 클릭시, Debug 용.
    @OnClick(R.id.btn_select)
    void deleteUserBtnClicked() {

        final String userId = "3333";

        /* 호출 메소드
        * ( 주의사항 : deleteUser 메소드는 레트로핏 동기 통신이기때문에,
        *            Worker Thread 에서 호출해야 함 !!! ) */
        new Thread(new Runnable() {
            @Override
            public void run() {

                ServerAccessModule.getInstance().deleteUser(userId);
            }
        }).start();
        // Argu : userId = Preference에서 뽑음.
        // 기능 : 서버에서 유저 삭제.
        // return 1 : 삭제 성공  /  -1 : 삭제 실패
    }








/*

    private void loginTest(String userId, String name, int platform) {
        LoginController loginCon = LoginController.getInstance();
        loginCon.requestLogin(this, userId, name, ""+platform);
    }


    private void logoutTest(String userId) {

        DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1);
        Cursor cursor = null;

        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(userId));
        while(cursor.moveToNext()) {
            updateMyScoreTest(userId, cursor.getInt(1), cursor.getInt(2));
        }

        cursor = db.retrieve(StaticSQLite.retrieveItemSQL(userId));
        while(cursor.moveToNext()) {
            updateItemTest(userId,
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );
        }
        cursor.close();
        db.close();


    }

    private void gameFinished(String userId, int exp, int userLevel, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {

        DBManager db = new DBManager(this.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1);
        Cursor cursor = null;

        // SQLite에 MyScore, Item Update
        db.insert(StaticSQLite.updateMyScoreSQL(userId, exp, userLevel));
        db.insert(StaticSQLite.updateItemSQL(userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt));

        // SQLite에서 MyScore, Item 가져와서
        // 서버에 Update
        cursor = db.retrieve(StaticSQLite.retrieveMyScoreSQL(userId));
        while(cursor.moveToNext()) {
            Log.e("Get MYSCORE from SQLite", ""+cursor.getInt(1)+cursor.getInt(2));
            updateMyScoreTest(userId, cursor.getInt(1), cursor.getInt(2));
        }

        cursor = db.retrieve(StaticSQLite.retrieveItemSQL(userId));
        while(cursor.moveToNext()) {
            Log.e("Get ITEM from SQLite", ""+cursor.getInt(1)+cursor.getInt(2)+cursor.getInt(3)+cursor.getInt(4));
            updateItemTest(userId,
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            );
        }
        cursor.close();
        db.close();
    }


    private void insertMyScoreTest(String userId, int exp, int userLevel) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.insertMyScore(this, userId, exp, userLevel);
    }
    private void updateMyScoreTest(String userId, int exp, int userLevel) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.updateMyScore(this, userId, exp, userLevel);
    }
    private void retrieveMyScoreTest(String userId) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getMyScore(this, userId);
    }
    private void retrieveScores(String userId) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getScores(this, userId);
    }

   */
/* private void retrieveFriendsSocre(String userId) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getFriendsScore(this, userId);
    }*//*


    private void insertItemTest(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.insertItem(this, userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }
    private void updateItemTest(String userId, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.updateItem(this, userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }
    private void retrieveItemTest(String userId) {
        ItemController itemCon = ItemController.getInstance();
        itemCon.getItem(this, userId);
    }
*/

}

