package com.ticcorp.ticsong;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RankingTestActivity extends Activity {

    private ListView mListView;
    private ListViewAdapter mAdapter;
    private CustomPreference pref;
    private String userId;

    private boolean modeFriend = false; // 친구 보기일 때 true, 전체 보기일 때 false

    @Bind(R.id.txt_friend)
    TextView txt_friend;

    @Bind(R.id.txt_yourRank)
    TextView txt_yourRank;

    @Bind(R.id.img_profile)
    ImageView img_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_test);
        ButterKnife.bind(this);

        pref = pref.getInstance(this.getApplicationContext());
        userId = pref.getValue("userId", "userId");

        mListView = (ListView) findViewById(R.id.list_rank);
        ServerAccessModule.getInstance().retrieveTopRanker(userId);

        Log.i("Ticlog Rank", "userId : " + userId);
        Log.i("Ticlog Rank", ServerAccessModule.TOP_RANKER_LIST + "");

        Handler hd = new Handler();
        hd.postDelayed(new rankinghandler(), 500);
        /*
        for(int i = 0; i < ServerAccessModule.TOP_RANKER_LIST.size(); i++) {
            mAdapter.addItem((i + 1) + "", null,
                    ServerAccessModule.TOP_RANKER_LIST.get(i).getName(),
                    "EXP : " + ServerAccessModule.TOP_RANKER_LIST.get(i).getExp(),
                    "Lv. " + ServerAccessModule.TOP_RANKER_LIST.get(i).getUserLevel());
        }
        */

    }

    private class rankinghandler implements Runnable {
        public void run() {
            mAdapter = new ListViewAdapter(RankingTestActivity.this);
            mListView.setAdapter(mAdapter);

            if(modeFriend) {
                Log.i("ticlog friendList", ServerAccessModule.FRIEND_LIST.toString());
                Log.i("ticlog friendListSize", ServerAccessModule.FRIEND_LIST.size() + "");
                for(int i = 0; i < ServerAccessModule.FRIEND_LIST.size(); i++) {
                    mAdapter.addItem((i + 1) + "", "http://graph.facebook.com/" +
                                    ServerAccessModule.FRIEND_LIST.get(i).getUserId() + "/picture",
                            ServerAccessModule.FRIEND_LIST.get(i).getName(),
                            "EXP : " + ServerAccessModule.FRIEND_LIST.get(i).getExp(),
                            "Lv. " + ServerAccessModule.FRIEND_LIST.get(i).getUserLevel());
                }

            } else {
                Log.i("ticlog TopRankerList", ServerAccessModule.TOP_RANKER_LIST.toString());
                for(int i = 0; i < ServerAccessModule.TOP_RANKER_LIST.size(); i++) {
                 mAdapter.addItem((i + 1) + "", "http://graph.facebook.com/" +
                         ServerAccessModule.TOP_RANKER_LIST.get(i).getUserId() + "/picture",
                        ServerAccessModule.TOP_RANKER_LIST.get(i).getName(),
                        "EXP : " + ServerAccessModule.TOP_RANKER_LIST.get(i).getExp(),
                        "Lv. " + ServerAccessModule.TOP_RANKER_LIST.get(i).getUserLevel());
                }
            }
        }
    }

    @OnClick(R.id.btn_exit)
    void exitClick() {
        startActivity(new Intent(RankingTestActivity.this, MainActivity.class));
        RankingTestActivity.this.finish();
    }

    @OnClick(R.id.btn_change)
    void changeClick() {
        if(modeFriend) {
            // 전체 보기로 전환
            modeFriend = false;
            Log.i("Ticlog Rank", "userId : " + userId);
            for(int j = 0; j < mAdapter.getCount(); j++){
                mAdapter.remove(j);
            }

            ServerAccessModule.getInstance().retrieveTopRanker(userId);
            Handler hd = new Handler();
            hd.postDelayed(new rankinghandler(), 500);

        } else {
            // 친구 보기로 전환
            modeFriend = true;
            Log.i("Ticlog Rank", "userId : " + userId);
            for(int j = 0; j < mAdapter.getCount(); j++){
                mAdapter.remove(j);
            }

            List<String> fList = new ArrayList<String>();
            if (pref.getValue("friendCnt", 1) > 0) {
                for (int i = 0; i < pref.getValue("friendCnt", 1); i++) {
                    Log.i("Ticlog Rank", "friendCnt : " + pref.getValue("friendCnt", 1) + ", friendId" + i + " : " + pref.getValue("friendId" + i, 0));
                    fList.add(pref.getValue("friendId" + i, 0) + "");
                }

                Log.i("ticlog fList", fList.toString());
                ServerAccessModule.getInstance().retrieveFriendList(userId, fList);

                Handler hd = new Handler();
                hd.postDelayed(new rankinghandler(), 500);
            } else { // 등록된 친구가 없을 때
                Toast.makeText(RankingTestActivity.this, "게임에 가입한 친구가 없습니다, 친구를 초대해보세요!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
