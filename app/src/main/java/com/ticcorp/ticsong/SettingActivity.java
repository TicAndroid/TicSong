package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by JPark on 2016-09-05.
 */
public class SettingActivity  extends Activity {

    ApplicationClass appClass;

    CustomPreference pref;
    AboutUsDialog aboutUsDialog;

    @Bind(R.id.btn_exit)
    ImageButton btn_exit;
    @Bind(R.id.setting_music)
    ImageButton setting_music;
    @Bind(R.id.setting_fx)
    ImageButton setting_fx;

    public Animation btn_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        appClass = (ApplicationClass) getApplication();

        pref = pref.getInstance(this.getApplicationContext());

        btn_click = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);

        settingBtn();
    }

    @OnClick (R.id.btn_exit)
    void exitClick() {
        fxPlay(R.raw.btn_touch);
        btn_exit.startAnimation(btn_click);
        //startActivity(new Intent(SettingActivity.this, MainActivity.class));
        SettingActivity.this.finish();
    }

    @OnClick (R.id.setting_music)
    void settingMusicClick() {
        fxPlay(R.raw.btn_touch);
        if (pref.getValue("setting_music", true)) {
            pref.put("setting_music", false);
            Log.i("ticlog", "setting_music Changed : " + pref.getValue("setting_music", true));
            setting_music.setBackgroundResource(R.drawable.btn_off);
            appClass.bgmStop();
        } else {
            pref.put("setting_music", true);
            Log.i("ticlog", "setting_music Changed : " + pref.getValue("setting_music", true));
            setting_music.setBackgroundResource(R.drawable.btn_on);
            appClass.bgmPlay(R.raw.jellyfish_in_space);
        }
    }

    @OnClick (R.id.setting_fx)
    void settingFxClick() {
        if (pref.getValue("setting_fx", true)) {
            pref.put("setting_fx", false);
            Log.i("ticlog", "setting_fx Changed : " + pref.getValue("setting_fx", true));
            setting_fx.setBackgroundResource(R.drawable.btn_off);
        } else {
            fxPlay(R.raw.btn_touch);
            pref.put("setting_fx", true);
            Log.i("ticlog", "setting_fx Changed : " + pref.getValue("setting_fx", true));
            setting_fx.setBackgroundResource(R.drawable.btn_on);
        }
    }

    @OnClick (R.id.btn_tutorial)
    void tutorialClick() {
        fxPlay(R.raw.btn_touch);
        startActivity(new Intent(SettingActivity.this, TutorialActivity.class));
    }

    @OnClick (R.id.btn_info)
    void infoClick() {
        fxPlay(R.raw.btn_touch);
        startActivity(new Intent(SettingActivity.this, InfoActivity.class));
    }

    @OnClick (R.id.btn_ask)
    void askClick() {
        fxPlay(R.raw.btn_touch);
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("plain/text");
        // 수신인 주소, 배열에 여러 주소를 넣을 경우 다수의 수신자에게 발송됨
        String[] toString = { "wemetinsummer@gmail.com" };
        mailIntent.putExtra(Intent.EXTRA_EMAIL, toString);
        // 메일 제목 미리 지정하기
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "[TicSong] 문의사항");
        // 메일 내용 미리 지정하기
        // mailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(mailIntent);
    }

    @OnClick (R.id.btn_aboutus)
    void aboutUsClick() {
        fxPlay(R.raw.btn_touch);
        aboutUsDialog = new AboutUsDialog(this);
        aboutUsDialog.show();

    }

    @OnClick (R.id.btn_opensource)
    void openSourceClick() {
        fxPlay(R.raw.btn_touch);
        startActivity(new Intent(SettingActivity.this, OpenSourceActivity.class));
    }

    @OnClick (R.id.btn_logout)
    void logoutClick() {
        // 로그아웃 임시 토스트 (후에 페이스북 버튼 로그아웃 기능 붙일 것)
        fxPlay(R.raw.btn_touch);
        Toast.makeText(SettingActivity.this, "Facebook 앱에서 로그아웃 해주세요.", Toast.LENGTH_LONG).show();
    }

    @OnClick (R.id.btn_out)
    void withdrawClick() {
        // 탈퇴 버튼
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("정말 탈퇴하시겠습니까?")
                .setContentText("삭제된 정보는 복구되지 않습니다. 그래도 괜찮습니까?")
                .setCancelText("취소")
                .setConfirmText("탈퇴하기")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        fxPlay(R.raw.btn_touch);
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        fxPlay(R.raw.btn_touch);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                ServerAccessModule.getInstance().deleteUser(pref.getValue("userId", "userId"));
                                pref.remove("userId");
                                Toast.makeText(SettingActivity.this, "탈퇴가 완료되었습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplication(), FBActivity.class);
                                startActivity(intent);
                                SettingActivity.this.finish();
                            }
                        }).start();
                    }
                })
                .show();
    }

    protected void settingBtn() {
        if(pref.getValue("setting_music", true)) {
            setting_music.setBackgroundResource(R.drawable.btn_on);
        } else {
            setting_music.setBackgroundResource(R.drawable.btn_off);
        }
        if(pref.getValue("setting_fx", true)) {
            setting_fx.setBackgroundResource(R.drawable.btn_on);
        } else {
            setting_fx.setBackgroundResource(R.drawable.btn_off);
        }
    }

    public void fxPlay(int target) {
        // 효과음 설정이 되어있을 경우 효과음 재생
        if (pref.getValue("setting_fx", true)) {
            MediaPlayer fxPlayer = new MediaPlayer();
            fxPlayer = MediaPlayer.create(SettingActivity.this, target);
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

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
