package com.ticcorp.ticsong;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.util.Log;

import com.ticcorp.ticsong.model.CustomPreference;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;


public class ApplicationClass extends Application {

    CustomPreference pref;
    MediaPlayer bgmPlayer;
    int bgmStatus = 0; // 0 : bgm 생성 전(release 후), 1 : bgm 재생 중, 2 : bgm 일시정지 중

    //어플리케이션이 시작할 때 실행
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "NanumBarunGothicBold.otf"))
                .addNormal(Typekit.createFromAsset(this, "Exo-BlackItalic.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "Exo-BoldItalic.otf"))
                .addItalic(Typekit.createFromAsset(this, "Exo-SemiBoldItalic.otf"));
        pref = pref.getInstance(this.getApplicationContext());
    }

    //화면이 바뀌면 실행
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void bgmPlay(int target) {
        // 정답 음악 듣기 설정이 되어있을 경우 배경음 재생
        if (bgmStatus == 0) {
            if (pref.getValue("setting_music", true)) {
                bgmPlayer = new MediaPlayer();
                bgmPlayer = MediaPlayer.create(getApplicationContext(), target);
                bgmPlayer.setLooping(true);
                bgmPlayer.start();
                bgmStatus = 1;
            }
        }
    }

    public void bgmStop() { // 정지 후 release
        if(bgmStatus == 1 || bgmStatus == 2) {
            if (bgmPlayer.isPlaying()) {
                bgmPlayer.stop();
                bgmPlayer.release();
                bgmStatus = 0;
            }
        }
    }

    public void bgmPause() { // 일시 정지
        if (bgmStatus == 1) {
            bgmPlayer.pause();
            bgmStatus = 2;
        }
    }

    public void bgmResume() { // 계속 재생
        if (bgmStatus == 2) {
            if (pref.getValue("setting_music", true)) {
                bgmPlayer.start();
                bgmStatus = 1;
            }
        }
    }

}