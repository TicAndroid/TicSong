package eu.jpark.ticsong.core;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import eu.jpark.ticsong.MainActivity;
import eu.jpark.ticsong.StaticInfo;
import eu.jpark.ticsong.retrofit2Interface.LoginInterface;
import eu.jpark.ticsong.model.CustomPreference;
import eu.jpark.ticsong.DTO.UserDTO;


import eu.jpark.ticsong.StaticInfo;
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


    public boolean requestLogin(final AppCompatActivity activity,String userId,String name,String platform) {
        /*
        원래코드

        아래는 바뀐코드이다.
        Retrofit 생성하는 부분을 랩해놓은 부분. 근데 없어도 될듯??조금더 생각해보자.*/
        //retrofit = RetrofitCreator.getRetrofit(LOGIN_URL);
        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();

        /*요청보낼 interface 객체 생성.*/
        LoginInterface loginInterface = retrofit.create(LoginInterface.class);

        /*서버로 요청을 보낼 객체생성.*/
        Call<UserDTO> call = loginInterface.requestLogin(userId, name,platform);

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
