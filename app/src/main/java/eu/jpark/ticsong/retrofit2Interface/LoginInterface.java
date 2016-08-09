package eu.jpark.ticsong.retrofit2Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import eu.jpark.ticsong.DTO.UserDTO;

public interface LoginInterface {
    @FormUrlEncoded
    @POST("login.do")
    Call<UserDTO> requestLogin(
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("platform") String platform
    );
}