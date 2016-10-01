package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by daesub on 2016. 10. 2..
 */

public class KaKaoTalkActivity extends Activity {

    @Bind(R.id.kakao_logout)
    Button logout;

    ApplicationClass appClass;
    CustomPreference pref;
    Context mContext;
    private SessionCallback callback;      //콜백 선언

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test_kakaotalk);

        ButterKnife.bind(this);

        mContext = getApplicationContext();

        appClass = (ApplicationClass) getApplication();
        pref = pref.getInstance(this.getApplicationContext());


        callback = new SessionCallback();                  // 이 두개의 함수 중요함
        Session.getCurrentSession().addCallback(callback);

    }

    @OnClick(R.id.kakao_logout)
    void logout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            Log.e("KakaoSession", "Opend");
            requestMe();

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login_test_kakaotalk); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    protected void requestMe() {

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환
                redirectMainActivity(userProfile); // 로그인 성공시 MainActivity로
            }
        });
    }

    private void redirectMainActivity(UserProfile userProfile) {

        final String kakaoID = String.valueOf(userProfile.getId()); // userProfile에서 ID값을 가져옴
        final String kakaoNickname = userProfile.getNickname();     // Nickname 값을 가져옴
        final String kakaoProfileImg = userProfile.getProfileImagePath(); // ProfileImage 값을 가져옴


        Log.e("User", "UserProfile : " + kakaoID + " / " + kakaoNickname + " / "  + kakaoProfileImg);

        pref.put("userId", kakaoID);
        pref.put("name", kakaoNickname);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerAccessModule.getInstance().login(getApplicationContext(), kakaoID, kakaoNickname, 1);
                startActivity(new Intent(getApplication(), MainActivity.class));
                finish();
            }
        }).start();


    }
    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, KaKaoTalkActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
