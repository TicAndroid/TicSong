package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONObject;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by daesub on 2016. 10. 5..
 */

public class LoginTestActivity extends Activity {

    private ApplicationClass appClass;
    private Context mContext;
    private CustomPreference pref;


    // Login User Platform.
    // Facebook : 0 / KaKaoTalk : 1
    private int LOGIN_PLATFORM_FLAG = 0;

    // for User Profile Setting
    private String userId = "";
    private String userName = "";
    private String userProfileImg = "";

    // for FaceBook
    private Profile profile;
    private CallbackManager callbackManager;
    //private AccessToken token;

    // for KakaoTalk
    private SessionCallback mKakaoCallback;

    // Custom LoginButton binding.
    @Bind(R.id.kakao_login_btn_test)
    Button kakaoLogin;
    @Bind(R.id.fb_login_btn_test)
    Button fbLogin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);

        ButterKnife.bind(this);

        mContext = getApplicationContext();
        appClass = (ApplicationClass) getApplication();
        pref = pref.getInstance(this.getApplicationContext());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (LOGIN_PLATFORM_FLAG) {
            case 0 : // Facebook Login
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
            case 1 : // KaKaoTalk Login
                if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                    return;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        appClass.bgmStop();
        switch (LOGIN_PLATFORM_FLAG) {
            case 0 : // Facebook
                break;
            case 1 : // KakaoTalk
                Session.getCurrentSession().removeCallback(mKakaoCallback);
                break;
        }
    }
    @Override
    protected void onPause() { // 화면이 가려졌을 때
        super.onPause();
        appClass.bgmPause();
    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    /////// 페이스북 로그인버튼 OnClick ///////
    @OnClick(R.id.fb_login_btn_test)
    void fbBtnClicked() {
        // 로그인 플랫폼 설정
        LOGIN_PLATFORM_FLAG = 0;
        isFacebookLogin(); // 호출
    }
    private void isFacebookLogin() {

        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginManager.getInstance().logOut();
        if(!pref.getValue("userId", "userId").equals("userId")) {
            pref.removeKey("userId");
        } // <-- 이거 사실 잘 모르겠음.. 왜 하는거야? - 대섭

        // 페이스북 App을 깔았을 경우, 현재 토큰을 확인하여,
        // 기존 로그인 회원은 자동으로 로그인시켜 MainActivity로 넘겨주는 부분이 필요함.

        //token = AccessToken.getCurrentAccessToken(); // 현재 페이스북 토큰을 가져옴
        /*if (token != null) {
            userId = token.getUserId();
            requestFacebookProfile(token);
        } else {*/

            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions(LoginTestActivity.this, Arrays.asList("public_profile", "email"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            AccessToken fbToken = loginResult.getAccessToken();
                            userId = loginResult.getAccessToken().getUserId();

                            // 페이스북 프로필 요청 메소드 호출
                            GraphRequest request = requestFacebookProfile(fbToken);

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender, birthday");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                            Log.e("Facebook Login", "로그인 취소");
                        }

                        @Override
                        public void onError(FacebookException error) {
                            error.printStackTrace();
                        }
                    });

        //}

    }
    // 페이스북 프로필 요청
    private GraphRequest requestFacebookProfile(AccessToken fbToken) {
        GraphRequest request = GraphRequest.newMeRequest(fbToken,
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("Facebook Login", "로그인 결과 / "+ response.toString());

                        try {
                            userName = object.getString("name");
                            userProfileImg = "http://graph.facebook.com/" + userId + "/picture?type=large";

                            // 페이스북 유저의 프로필 세팅 완료후 호출
                            redirectToMainActivity();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        return request;
    }


    /////// 카카오톡 로그인버튼 OnClick ///////
    @OnClick(R.id.kakao_login_btn_test)
    void kakaoBtnClicked() {
        // 로그인 Platform 설정
        LOGIN_PLATFORM_FLAG = 1;
        isKakaoLogin();
    }
    private void isKakaoLogin() {

        // 카카오톡 Session이 열려있으면 강제로 close
        if (Session.getCurrentSession().isOpened()) {
            //Log.e("카톡 로그인 세선", "Opened !!");
            Session.getCurrentSession().close();
        }

        // 카카오 세션을 오픈한다
        mKakaoCallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaoCallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginTestActivity.this);
    }
    // 카카오톡 로그인을 위한 SessionCallback Inner 함수
    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.d("KakaoTalk Login", "Session Opened");
            // 카카오톡 프로필 요청 메소드 호출
            requestKaKaoProfile();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                //Logger.e(exception);
                Log.e("KakaoTalk Login", "Session Open Failed");
            }
            redirectToLoginActivity(); // 세션 연결이 실패했을때  로그인화면을 다시 불러옴
            //setContentView(R.layout.activity_login_test); // 세션 연결이 실패했을때  로그인화면을 다시 불러옴
        }
    }
    // 카카오톡 프로필 요청
    protected void requestKaKaoProfile() {

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.e(message);
                Log.e("KakaoProfile", "Session Failed");

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    redirectToLoginActivity();
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("KakaoProfile", "Session Closed");
                redirectToLoginActivity();
            }

            @Override
            public void onNotSignedUp() {} // 카카오톡 회원이 아닐 시 showSignup(); 호출해야함

            @Override
            public void onSuccess(UserProfile userProfile) {  //성공 시 userProfile 형태로 반환

                // 유저의 id, name, progileImg 세팅
                userId = String.valueOf(userProfile.getId()); // userProfile에서 ID값을 가져옴
                userName = userProfile.getNickname();     // Nickname 값을 가져옴
                userProfileImg = userProfile.getProfileImagePath(); // ProfileImage 값을 가져옴

                // 카카오톡 유저의 프로필 세팅을 완료 후에 호출.
                redirectToMainActivity();
            }
        });
    }



    // 각 Platform 별 프로필 세팅 완료후 MainActivity로 이동하기 위한 전과정.
    private void redirectToMainActivity() {

        // 이건 배포전에 지워야함 - 대섭
        Log.e("User", "UserProfile : " + userId + " / " + userName + " / "  + userProfileImg);

        new Thread(new Runnable() {
            @Override
            public void run() {

                // 서버에 로그인 요청
                ServerAccessModule.getInstance().login(getApplicationContext(), userId, userName, LOGIN_PLATFORM_FLAG);

                // pref 에 set
                pref.put("userId", userId);
                pref.put("name", userName);
                pref.put("profileImg", userProfileImg);

                appClass.bgmPlay(R.raw.jellyfish_in_space);
                Log.v("test"," "+pref.getValue("tutorial",-1));
                switch (pref.getValue("tutorial", -1)) {
                    case 0: // 약관 동의를 하지 않은 경우
                        Log.v("test","test2");
                        startActivity(new Intent(getApplication(), AgreementActivity.class));
                        LoginTestActivity.this.finish();
                        break;
                    case 1: // 약관 동의는 했으나 튜토리얼 보는 중에 앱을 종료한 경우
                        Log.v("test","test3");
                        startActivity(new Intent(getApplication(), TutorialActivity.class));
                        LoginTestActivity.this.finish();
                        break;
                    case 2: // 약관 동의와 튜토리얼을 모두 완료한 경우

                        // 정상적으로 메인으로 이동
                        Log.v("test","test4");
                        startActivity(new Intent(getApplication(), MainActivity.class));
                        LoginTestActivity.this.finish();

                        break;
                    default:
                        Log.v("test","test5");
                        pref.put("tutorial", 0);
                        startActivity(new Intent(getApplication(), AgreementActivity.class));
                        LoginTestActivity.this.finish();
                        break;
                }
            }
        }).start();

    }


    // 로그인 실패시 로그인페이지로 다시 이동
    protected void redirectToLoginActivity() {
        final Intent intent = new Intent(this, LoginTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
