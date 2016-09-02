package com.ticcorp.ticsong.module;

import android.content.Context;
import android.util.Log;

import com.ticcorp.ticsong.DTO.FriendsScoreView;
import com.ticcorp.ticsong.DTO.ScoreView;
import com.ticcorp.ticsong.core.ItemController;
import com.ticcorp.ticsong.core.LoginController;
import com.ticcorp.ticsong.core.MyScoreController;

import java.util.List;

/**
 * Created by Daesub Kim on 2016-08-18.
 */
public class ServerAccessModule {

    public static List<ScoreView> TOP_RANKER_LIST;
    public static List<FriendsScoreView> FRIEND_LIST;

    private static ServerAccessModule instance;
    static {
        instance = new ServerAccessModule();
    }
    public static ServerAccessModule getInstance() {
        if(instance !=null)
            return instance;
        else
            return new ServerAccessModule();
    }
    private ServerAccessModule() {}

    /**
     * 로그인. 서버에서 값을 가져옴. 새로가입 유저일 경우, 서버에서 register.
     * @param context
     * @param userId
     * @param name
     * @param platform
     */
    public void login(Context context, String userId, String name, int platform) {
        LoginController loginCon = LoginController.getInstance();
        loginCon.requestLogin(context, userId, name, ""+platform);
    }

    /**
     * SQLite 에서 값을 꺼내서, 서버로 전송.
     * @param userId
     */
    public void logout(String userId, int exp, int userLevel, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        gameFinished(userId, exp, userLevel, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }

    /**
     * 5곡 게임이 끝난 후, 호출. 서버로 전송. CustomPreference 에서 꺼내서 argu에 넣어준다.
     * @param userId
     * @param exp
     * @param userLevel
     * @param item1Cnt
     * @param item2Cnt
     * @param item3Cnt
     * @param item4Cnt
     */
    public void gameFinished (String userId, int exp, int userLevel, int item1Cnt, int item2Cnt, int item3Cnt, int item4Cnt) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.updateMyScore(userId, exp, userLevel);

        ItemController itemCon = ItemController.getInstance();
        itemCon.updateItem(userId, item1Cnt, item2Cnt, item3Cnt, item4Cnt);
    }

    /**
     * Top Ranker를 불러온다.
     * @param userId
     */
    public void retrieveTopRanker(String userId) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getScores(userId);
    }

    /**
     * 친구 목록/점수를 불러온다.
     * @param userId
     * @param friendIdList Facebook or KaKaoTalk 친구목록 List<String>
     */
    public void retrieveFriendList(String userId, List<String> friendIdList) {
        MyScoreController scoreCon = MyScoreController.getInstance();
        scoreCon.getFriendsScore(userId, friendIdList);
    }

}
