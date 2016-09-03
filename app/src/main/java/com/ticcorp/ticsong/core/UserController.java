package com.ticcorp.ticsong.core;

import android.util.Log;

import com.ticcorp.ticsong.StaticInfo;
import com.ticcorp.ticsong.retrofit2Interface.UserInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daesub Kim on 2016-09-03.
 */
public class UserController {

    private static UserController userController;

    private Retrofit retrofit = null;
    /**a
     * 성공 했는지 아닌지
     */
    private boolean isSuccess=false;

    public boolean getIsSuccess(){return this.isSuccess;}

    static {
        userController = new UserController();
    }

    public static UserController getInstance() {
        if(userController==null)
            userController= new UserController();
        return userController;
    }


    public int deleteUser(String userId) {

        retrofit = new Retrofit.Builder().baseUrl(StaticInfo.TICSONG_BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        UserInterface userInterface = retrofit.create(UserInterface.class);

        Call<Integer> call = userInterface.deleteUser("delete", userId);
        int result = 0;
        try {
            result = call.execute().body();
            if(result == -1){
                Log.d("탈퇴_실패 -", "탈퇴 오류"); // 디버깅용
                return -1;
            }
            Log.d("탈퇴_성공 -", "탈퇴 Success"); // 디버깅용
            return 1;

        } catch (IOException ie) {
            ie.printStackTrace();
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
