package com.ticcorp.ticsong.core;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ticcorp.ticsong.DTO.LoginView;
import com.ticcorp.ticsong.MainActivity;
import com.ticcorp.ticsong.StaticInfo;
import com.ticcorp.ticsong.model.DBManager;
import com.ticcorp.ticsong.model.StaticSQLite;
import com.ticcorp.ticsong.module.SQLiteAccessModule;
import com.ticcorp.ticsong.retrofit2Interface.LoginInterface;
import com.ticcorp.ticsong.model.CustomPreference;
import com.ticcorp.ticsong.DTO.UserDTO;


import com.ticcorp.ticsong.StaticInfo;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daesub Kim on 2016-07-31.
 */
public class LoginController {
    private static LoginController loginController;

    private Retrofit retrofit = null;
    /**a
     * 성공 했는지 아닌지
     */
    private boolean isSuccess=false;

    public boolean getIsSuccess(){return this.isSuccess;}

    static {
        loginController = new LoginController();
    }


    public static LoginController getInstance() {
        if(loginController==null)
            loginController= new LoginController();
        return loginController;
    }


    public boolean requestLogin(final Context context, String userId, String name, String platform) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        LoginInterface loginInterface = retrofit.create(LoginInterface.class);
        Call<LoginView> call = loginInterface.requestLogin(userId, name,platform);
        LoginView loginView = null;
        try {
            loginView = call.execute().body();
            if(loginView == null) {
                Log.d("로그인_실패 -", "아이디 or 이름 오류"); // 디버깅용
                return false;
            }
            /*if(call.execute().isSuccessful()) {

            } else {
                Log.d("로그인_실패 -", "isFail"); // 디버깅용
                return false;
            }*/

            CustomPreference customPreference = CustomPreference.getInstance(context);

            customPreference.put("login",true);
            customPreference.put("userId",loginView.getUserId());
            customPreference.put("name",loginView.getName());
            customPreference.put("platform",loginView.getPlatform());
            customPreference.put("exp",loginView.getExp());
            customPreference.put("userLevel",loginView.getUserLevel());
            customPreference.put("item1Cnt",loginView.getItem1Cnt());
            customPreference.put("item2Cnt",loginView.getItem2Cnt());
            customPreference.put("item3Cnt",loginView.getItem3Cnt());
            customPreference.put("item4Cnt",loginView.getItem4Cnt());

            Log.e("login_login","true");
            Log.e("Login User Info", loginView.toString());

                /* SQLite DB Insert */
            //SQLiteAccessModule.getInstance(context).login(loginView);

            return true;
        } catch (IOException ie) {
            ie.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean requestAsyncLogin (final Context context, String userId, String name, String platform) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);
        /*서버로 요청을 보낼 객체생성.*/
        Call<LoginView> call = loginInterface.requestLogin(userId, name,platform);
        call.clone().enqueue(new Callback<LoginView>() {
            @Override
            public void onResponse(Call<LoginView> call, Response<LoginView> response) {

                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 실패", "-1");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                LoginView loginView = response.body();
                if(loginView == null) {
                    Log.d("로그인_실패 -", "아이디 or 이름 오류"); // 디버깅용
                    return;
                }

                CustomPreference customPreference = CustomPreference.getInstance(context);

                customPreference.put("login",true);
                customPreference.put("userId",loginView.getUserId());
                customPreference.put("name",loginView.getName());
                customPreference.put("platform",loginView.getPlatform());
                customPreference.put("exp",loginView.getExp());
                customPreference.put("userLevel",loginView.getUserLevel());
                customPreference.put("item1Cnt",loginView.getItem1Cnt());
                customPreference.put("item2Cnt",loginView.getItem2Cnt());
                customPreference.put("item3Cnt",loginView.getItem3Cnt());
                customPreference.put("item4Cnt",loginView.getItem4Cnt());

                /* SQLite DB Insert */
                SQLiteAccessModule.getInstance(context).login(loginView);

                /*DBManager db = new DBManager(context, StaticSQLite.TICSONG_DB, null, 1 );
                db.insert(StaticSQLite.insertUserSQL(loginView.getUserId(), loginView.getName(), loginView.getPlatform()));
                db.insert(StaticSQLite.insertMyScoreSQL(loginView.getUserId(), loginView.getExp(), loginView.getUserLevel()));
                db.insert(StaticSQLite.insertItemSQL(loginView.getUserId(), loginView.getItem1Cnt(), loginView.getItem2Cnt()
                        , loginView.getItem3Cnt(), loginView.getItem4Cnt()));
                db.close();*/

                Log.e("login_login","true");
                Log.e("Login User Info", loginView.toString());
            }

            @Override
            public void onFailure(Call<LoginView> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

    public boolean requestLogin(final Activity activity, String userId, String name, String platform) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<LoginView> call = loginInterface.requestLogin(userId, name,platform);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다.
        * */
        call.clone().enqueue(new Callback<LoginView>() {
            @Override
            public void onResponse(Call<LoginView> call, Response<LoginView> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 실패", "-1");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                LoginView loginView = response.body();
                if(loginView == null) {
                    Log.d("로그인_실패 -", "아이디 or 이름 오류"); // 디버깅용
                    return;
                }

                CustomPreference customPreference = CustomPreference.getInstance(activity.getApplicationContext());

                customPreference.put("login",true);
                customPreference.put("userId",loginView.getUserId());
                customPreference.put("name",loginView.getName());
                customPreference.put("platform",loginView.getPlatform());
                customPreference.put("exp",loginView.getExp());
                customPreference.put("userLevel",loginView.getUserLevel());
                customPreference.put("item1Cnt",loginView.getItem1Cnt());
                customPreference.put("item2Cnt",loginView.getItem2Cnt());
                customPreference.put("item3Cnt",loginView.getItem3Cnt());
                customPreference.put("item4Cnt",loginView.getItem4Cnt());


                /* DB Insert */
                DBManager db = new DBManager(activity.getApplicationContext(), StaticSQLite.TICSONG_DB, null, 1 );
                db.insert(StaticSQLite.insertUserSQL(loginView.getUserId(), loginView.getName(), loginView.getPlatform()));
                db.insert(StaticSQLite.insertMyScoreSQL(loginView.getUserId(), loginView.getExp(), loginView.getUserLevel()));
                db.insert(StaticSQLite.insertItemSQL(loginView.getUserId(), loginView.getItem1Cnt(), loginView.getItem2Cnt()
                        , loginView.getItem3Cnt(), loginView.getItem4Cnt()));
                db.close();



                Log.e("login_login","true");
                Log.e("Login User Info", loginView.toString());
                /*Log.e("login Name", customPreference.getValue("name", userDTO.getName()));
                Log.e("login Platform", ""+customPreference.getValue("platform", userDTO.getPlatform()));
*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/
            }

            @Override
            public void onFailure(Call<LoginView> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

    public boolean requestLogout(final Context context, String userId) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);
        Call<LoginView> call = loginInterface.requestLogin(userId, "","");
        call.clone().enqueue(new Callback<LoginView>() {
            @Override
            public void onResponse(Call<LoginView> call, Response<LoginView> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 실패", "-1");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                LoginView loginView = response.body();
                if(loginView == null) {
                    Log.d("로그인_실패 -", "아이디 or 이름 오류"); // 디버깅용
                    return;
                }

                CustomPreference customPreference = CustomPreference.getInstance(context);

                customPreference.put("login",true);
                customPreference.put("userId",loginView.getUserId());
                customPreference.put("name",loginView.getName());
                customPreference.put("platform",loginView.getPlatform());
                customPreference.put("exp",loginView.getExp());
                customPreference.put("userLevel",loginView.getUserLevel());
                customPreference.put("item1Cnt",loginView.getItem1Cnt());
                customPreference.put("item2Cnt",loginView.getItem2Cnt());
                customPreference.put("item3Cnt",loginView.getItem3Cnt());
                customPreference.put("item4Cnt",loginView.getItem4Cnt());


                /* DB Insert */
                DBManager db = new DBManager(context, StaticSQLite.TICSONG_DB, null, 1 );
                db.create();
                db.insert(StaticSQLite.insertUserSQL(loginView.getUserId(), loginView.getName(), loginView.getPlatform()));
                db.insert(StaticSQLite.insertMyScoreSQL(loginView.getUserId(), loginView.getExp(), loginView.getUserLevel()));
                db.insert(StaticSQLite.insertItemSQL(loginView.getUserId(), loginView.getItem1Cnt(), loginView.getItem2Cnt()
                        , loginView.getItem3Cnt(), loginView.getItem4Cnt()));
                db.close();



                Log.e("login_login","true");
                Log.e("Login User Info", loginView.toString());
                /*Log.e("login Name", customPreference.getValue("name", userDTO.getName()));
                Log.e("login Platform", ""+customPreference.getValue("platform", userDTO.getPlatform()));
*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/
            }

            @Override
            public void onFailure(Call<LoginView> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }

    public boolean oldLogin(final Activity activity, String userId, String name, String platform) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<UserDTO> call = loginInterface.oldLogin(userId, name,platform);

        /*Call은 동기화 클래스이다.
        * 한번 요청을 보낸 다음, 재 요청을 보낼 경우 에러가 발생한다.
        * 그렇기 때문에 값싼 clone()메소드를 호출하여 복사하고,
        * 복사한 Call객체로 요청큐에 넣는다. */
        call.clone().enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                /* 응답코드가 200번대가 아니라면*/
                if(!response.isSuccess()) {
                    Log.d("로그인 코드_",response.body().getResultCode()+"");
                    return ; // 아무 코드를 실행하지 않고 리턴.
                }
                Log.d("로그인_성공코드 -", response.code() + ""); // 디버깅용

                UserDTO userDTO = response.body();
                if(userDTO.getResultCode().equals("0")) {
                    Log.d("로그인_실패 -", "아이디 or 이름 오류"); // 디버깅용
                    return;
                }

                CustomPreference customPreference = CustomPreference.getInstance(activity);

                customPreference.put("userId",userDTO.getUserId());
                customPreference.put("name",userDTO.getName());
                customPreference.put("platform",userDTO.getPlatform());
                customPreference.put("login",true);
                Log.e("login_login","true");

                /*Log.e("login User ID", customPreference.getValue("userId", userDTO.getUserId()));
                Log.e("login Name", customPreference.getValue("name", userDTO.getName()));
                Log.e("login Platform", ""+customPreference.getValue("platform", userDTO.getPlatform()));
*/
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.startActivity(new Intent(activity, MainActivity.class));
                            }
                        });
                    }
                }).start();*/
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                isSuccess = false;
                Log.d("로그인_실패코드-",call.toString()+"__"+t.getMessage());
                Log.d("로그인_왜실패?",t.toString());
            }
        });
        return isSuccess;
    }
}
