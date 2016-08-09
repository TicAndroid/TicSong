package eu.jpark.ticsong;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import eu.jpark.ticsong.core.LoginController;
import eu.jpark.ticsong.core.RegisterController;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.btn_con:
                Toast.makeText(this, "접속합니다", Toast.LENGTH_SHORT).show();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

                mGoogleApiClient.connect();

                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "구글 플레이 연결이 되었습니다.");

        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

            Log.d(TAG, "onConnected 연결 실패");
            login("123123", "Daesub");
            register("44444444", "Test Account 01");

        } else {
            Log.d(TAG, "onConnected 연결 성공");

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(TAG,"디스플레이 이름 : "+ currentPerson.getDisplayName());
                Log.d(TAG, "디스플레이 아이디는 : " + currentPerson.getId());


                // 여기서 서버 연결.
                /* 서버로 부터 JSON 객체를 받아 SQLite 에 저장. */
                //putJsonData();


                //login(currentPerson.getDisplayName(), currentPerson.getId());
                register(currentPerson.getId(), currentPerson.getDisplayName());


            }
            login("123123", "Daesub");

            startActivity(new Intent(getApplication(), MainActivity.class)); // 로딩이 끝난후 이동할 Activity
            LoginActivity.this.finish(); // 로딩페이지 Activity Stack에서 제거

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "연결 에러 " + connectionResult);

        login("123123", "Daesub");
        register("333333", "Test Account 01");


        if (connectionResult.hasResolution()) {

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                //이게 핵심?
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString(), e);
            }
        }else{
            Toast.makeText(getApplicationContext(), "이미 로그인 중", Toast.LENGTH_SHORT).show();
        }
    }


    private void putJsonData() {
/*
        LoginController loginController = LoginController.getInstance();
        loginController.requestLogin(this, inputId.getText().toString(), inputPwd.getText().toString());*/

    }

    private void login(String userId, String name) {
        LoginController loginController = LoginController.getInstance();
        //loginController.requestLogin(this, userId, name);
        //loginController.requestLogin(this, "123123", "Daesub");
    }

    private void register(String userId, String name) {

        Log.e("Name !!!!!! ", name);

        RegisterController registerController = RegisterController.getInstance();
        //registerController.register(this, userId, name);
    }


}
