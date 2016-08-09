package eu.jpark.ticsong.retrofit2Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import eu.jpark.ticsong.DTO.UserDTO;
/**
 * Created by Daesub Kim on 2016-07-31.
 */
public interface RegisterInterface {
    @FormUrlEncoded
    @POST("register.do")
    Call<UserDTO> register(
            @Field("userId") String userId,
            @Field("name") String name,
            @Field("platform") String platform
    );
}
