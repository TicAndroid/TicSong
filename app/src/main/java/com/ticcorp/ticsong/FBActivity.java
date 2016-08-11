package com.ticcorp.ticsong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeon on 2016-08-10.
 */
public class FBActivity extends Activity {

    private CallbackManager callbackManager;
    String id = "";
    String name = "";
    String email = "";
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();


        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);


        ImageButton btn_start = (ImageButton) findViewById(R.id.btn_start);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), GameActivity.class));
            }
        });


        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {//로그인이 성공되었을때 호출
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
                                            String email1=object.getString("email");

                                            id = (String) response.getJSONObject().get("id");//페이스북 아이디값
                                            name = (String) response.getJSONObject().get("name");//페이스북 이름
                                            //email = (String) response.getJSONObject().get("email");//이메일
                                            Log.i("id", id);
                                            //Log.i("id1",id1);
                                            Log.i("name", name);
                                            //Log.i("email1",email1);
                                            Log.i("email", email);

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
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(FBActivity.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FBActivity.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());

        final Button loginBtn = (Button)findViewById(R.id.login_button);
        loginBtn.post(new Runnable() {
            @Override
            public void run() {
                //loginBtn.setVisibility(View.INVISIBLE);
            }
        });

    }
}