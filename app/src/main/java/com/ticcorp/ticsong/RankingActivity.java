package com.ticcorp.ticsong;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ticcorp.ticsong.activitySupport.CustomBitmapPool;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class RankingActivity extends Activity {

    ApplicationClass appClass;

    private ListView mListView, aListView;
    private ListViewAdapter mAdapter, aAdapter; //가상
    private CustomPreference pref;
    private String userId, user_profile_img;
    private boolean isLoaded = false; // 랭킹 로딩 확인

    public Animation btn_click;

    private boolean modeFriend = false; // 친구 보기일 때 true, 전체 보기일 때 false

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;

    @Bind(R.id.txt_friend)
    TextView txt_friend;

    @Bind(R.id.txt_yourRank)
    TextView txt_yourRank;

    @Bind(R.id.img_profile)
    ImageButton img_profile;

    @Bind(R.id.raking_standby)
    ImageView ranking_standby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        ButterKnife.bind(this);

        appClass = (ApplicationClass) getApplication();

        pref = pref.getInstance(this.getApplicationContext());
        userId = pref.getValue("userId", "userId");

        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

        mListView = (ListView) findViewById(R.id.list_rank);
        ServerAccessModule.getInstance().retrieveTopRanker(userId);

        //Log.i("Ticlog Rank", "userId : " + userId);

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

        // txt_friend.setText(pref.getValue("friendCnt", 1) + " 친구 "); // 2016.09.29 대섭 삭제
        txt_yourRank.setText("TOP 20 "); //자기가 몇 등인지 확인하는 방법이 없어 임시로 현재 랭킹 모드 표시

        // TOP20 문구 옆, 본인 프로필 이미지  // 2016.10.12 대섭 추가
        user_profile_img = pref.getValue("profileImg", "profileImg");


        Glide.with(this).load(user_profile_img).bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).
                error(R.drawable.profile_main_image).into(img_profile);


    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private class rankinghandler implements Runnable {
        public void run() {
            mAdapter = new ListViewAdapter(RankingActivity.this);
            mListView.setAdapter(mAdapter);

            if(modeFriend) {
                txt_yourRank.setText("FRIENDS ");
                ranking_standby.setVisibility(View.VISIBLE);
                //Log.i("ticlog friendList", ServerAccessModule.FRIEND_LIST.toString());
                //Log.i("ticlog friendListSize", ServerAccessModule.FRIEND_LIST.size() + "");
                for(int i = 0; i < ServerAccessModule.FRIEND_LIST.size(); i++) {
                    mAdapter.addItem((i + 1) + "", "http://graph.facebook.com/" +
                                    ServerAccessModule.FRIEND_LIST.get(i).getUserId() + "/picture?type=large",
                            ServerAccessModule.FRIEND_LIST.get(i).getName(),
                            "EXP : " + ServerAccessModule.FRIEND_LIST.get(i).getExp(),
                            "Lv. " + ServerAccessModule.FRIEND_LIST.get(i).getUserLevel());
                }
                ranking_standby.setVisibility(View.INVISIBLE);

            } else {
                txt_yourRank.setText("TOP 20 ");
                ranking_standby.setVisibility(View.VISIBLE);
                //Log.i("ticlog TopRankerList", ServerAccessModule.TOP_RANKER_LIST.toString());
                for(int i = 0; i < ServerAccessModule.TOP_RANKER_LIST.size(); i++) {
                    mAdapter.addItem((i + 1) + "", "http://graph.facebook.com/" +
                                    ServerAccessModule.TOP_RANKER_LIST.get(i).getUserId() + "/picture?type=large",
                            ServerAccessModule.TOP_RANKER_LIST.get(i).getName(),
                            "SCORE : " + ServerAccessModule.TOP_RANKER_LIST.get(i).getExp(),
                            "Lv. " + ServerAccessModule.TOP_RANKER_LIST.get(i).getUserLevel());
                }
                ranking_standby.setVisibility(View.INVISIBLE);
            }
        }
    }

    @OnClick(R.id.btn_exit)
    void exitClick() {
        fxPlay(R.raw.btn_touch);
        btn_exit.startAnimation(btn_click);
        startActivity(new Intent(RankingActivity.this, MainActivity.class));
        RankingActivity.this.finish();
    }

    // 친구목록보기 화면으로 전환을 막아놓음. // 2016.10.12 대섭
    //@OnClick(R.id.img_profile)
    void changeClick() {
        fxPlay(R.raw.btn_touch);
        if(modeFriend) {
            // 전체 보기로 전환
            modeFriend = false;
            //Log.i("Ticlog Rank", "userId : " + userId);
            for(int j = 0; j < mAdapter.getCount(); j++){
                mAdapter.remove(j);
            }

            ServerAccessModule.getInstance().retrieveTopRanker(userId);
            Handler hd = new Handler();
            hd.postDelayed(new rankinghandler(), 500);

        } else {
            if (pref.getValue("friendCnt", 1) > 0) {
                // 친구 보기로 전환
                modeFriend = true;
                //Log.i("Ticlog Rank", "userId : " + userId);
                for(int j = 0; j < mAdapter.getCount(); j++){
                    mAdapter.remove(j);
                }

                List<String> fList = new ArrayList<String>();
                for (int i = 0; i < pref.getValue("friendCnt", 1); i++) {
                    //Log.i("Ticlog Rank", "friendCnt : " + pref.getValue("friendCnt", "friendCnt") + ", friendId" + i + " : " + pref.getValue("friendId" + i, "friendId" + i));
                    fList.add(pref.getValue("friendId" + i, "friendId" + i) + "");
                }

                //Log.i("ticlog fList", fList.toString());
                ServerAccessModule.getInstance().retrieveFriendList(userId, fList);

                Handler hd = new Handler();
                hd.postDelayed(new rankinghandler(), 500);
            } else { // 등록된 친구가 없을 때
                Toast.makeText(RankingActivity.this, "게임에 가입한 친구가 없습니다, 친구를 초대해보세요!", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void fxPlay(int target) {
        // 효과음 설정이 되어있을 경우 효과음 재생
        if (pref.getValue("setting_fx", true)) {
            MediaPlayer fxPlayer = new MediaPlayer();
            fxPlayer = MediaPlayer.create(RankingActivity.this, target);
            fxPlayer.start();
            fxPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
        }
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
