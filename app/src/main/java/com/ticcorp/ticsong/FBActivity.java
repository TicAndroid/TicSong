package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.module.ServerAccessModule;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Jeon on 2016-08-10.
 */
//Login Activity
public class FBActivity extends Activity {

    private CallbackManager callbackManager;
    private Profile profile;
    String id = "";
    String name = "";
    String email = "";
    Context mContext;

    TextView user_name;
    AccessToken token; // 페이스북 로그인 토큰

    CustomPreference pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        pref = pref.getInstance(this.getApplicationContext());

        token = AccessToken.getCurrentAccessToken(); // 토큰을 가져옴

        FacebookSdk.sdkInitialize(getApplicationContext());

        LoginManager.getInstance().logOut();
        if(!pref.getValue("userId", "userId").equals("userId")) {
            pref.remove("userId");
        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(FBActivity.this, Arrays.asList("public_profile", "email"));
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_freinds");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {//로그인이 성공되었을때 호출
                        Log.i("ticlog", "onSuccess");
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        // Application code
                                        try {
                                            //String id1=object.getString("id");
                                            //String email1 = object.getString("email");

                                            id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                                            name = (String) response.getJSONObject().get("name");//페이스북 이름
                                            //email = (String) response.getJSONObject().get("email");//이메일
                                            Log.i("ticlog id", id);
                                            //Log.i("id1",id1);
                                            Log.i("ticlog name", name);
                                            //Log.i("email1",email1);
                                            pref.put("userId", id);

                                            new GraphRequest(
                                                    AccessToken.getCurrentAccessToken(),
                                                    "/me/friends",
                                                    null,
                                                    HttpMethod.GET,
                                                    new GraphRequest.Callback() {
                                                        public void onCompleted(GraphResponse response) {
                                                            /* handle the result */
                                                            Log.i("ticlog", response.toString());
                                                            JSONObject responseJSON = response.getJSONObject();
                                                            try {
                                                                JSONArray dataArray = responseJSON.getJSONArray("data");
                                                                pref.put("friendCnt", dataArray.length());
                                                                for (int i = 0; i < dataArray.length(); i++) {
                                                                    JSONObject friendData = dataArray.getJSONObject(i);
                                                                    String friendId = (String) friendData.get("id");
                                                                    pref.put("friendId" + i, friendId);
                                                                    Log.i("ticlog", "pref friendData" + i + " : " + friendData.toString() + " / " + friendId);
                                                                    Log.i("ticlog", "pref friendId" + i + " : " + pref.getValue("friendId" + i, "friendId" + i));
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                                Log.i("ticlog", "JSONException");
                                                            }
                                                        }
                                                    }
                                            ).executeAsync();

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ServerAccessModule.getInstance().login(getApplicationContext(), id, name, 0);

                                                    startActivity(new Intent(getApplication(), MainActivity.class));
                                                    //startActivity(new Intent(getApplication(), ServerTestActivity.class));
                                                    // 로그인 되면 현재 페이지 제거
                                                    FBActivity.this.finish();
                                                }
                                            }).start();

                                            /*ServerAccessModule.getInstance().login(getApplicationContext(), id, name, 0);

                                            startActivity(new Intent(getApplication(), MainActivity.class));
                                            // 로그인 되면 현재 페이지 제거
                                            FBActivity.this.finish();*/


                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        //서버에 id ,name,email등 정보를 보내고 조회후에 승인되면
                                        //고유 키를 받아서 sharedPreference에 저장
                                        //로그아웃하기 전까지 담아둠

                                        // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Log.i("ticlog", "onSuccess End");
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FBActivity.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
                        // App code
                        Log.i("ticlog", "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FBActivity.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
                        // App code
                        Log.i("ticlog", "onError");
                    }
                });
        if (token != null) {// 로그인 되어있으면 자동으로 게임화면으로
            /*Log.i("ticlog", "token = " + token.getToken());

            profile = Profile.getCurrentProfile();
            id = profile.getId();
            name = profile.getName();
            Log.i("ticlog", id + "/" + name);*/

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/me/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */
                            Log.i("ticlog", response.toString());
                        }
                    }
            ).executeAsync();

            //startActivity(new Intent(getApplication(), MainActivity.class));
            //FBActivity.this.finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());

        final Button loginBtn = (Button) findViewById(R.id.login_button);
        loginBtn.post(new Runnable() {
            @Override
            public void run() {
                //loginBtn.setVisibility(View.INVISIBLE);
                //startActivity(new Intent(getApplication(), MainActivity.class));
                // 로그인 되면 현재 페이지 제거
                //FBActivity.this.finish();
            }
        });

    }

    // 폰트 적용
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}