package com.ticcorp.ticsong.retrofit2Interface;

import com.ticcorp.ticsong.DTO.UserDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Daesub Kim on 2016-09-03.
 */
public interface UserInterface {

    @FormUrlEncoded
    @POST("user.do")
    Call<Integer> deleteUser(
            @Field("service") String service,
            @Field("userId") String userId
    );
}
