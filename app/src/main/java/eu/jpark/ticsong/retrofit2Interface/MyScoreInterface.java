package eu.jpark.ticsong.retrofit2Interface;

import eu.jpark.ticsong.DTO.MyScoreDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Daesub Kim on 2016-07-31.
 */
public interface MyScoreInterface {

    @FormUrlEncoded
    @POST("myscore.do")
    Call<MyScoreDTO> getMyScore(
            @Field("service") String service,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("myscore.do")
    Call<MyScoreDTO> insertMyScore(
            @Field("service") String service,
            @Field("userId") String userId,
            @Field("exp") int exp,
            @Field("userLevel") int userLevel
    );

    @FormUrlEncoded
    @POST("myscore.do")
    Call<MyScoreDTO> updateMyScore(
            @Field("service") String service,
            @Field("userId") String userId,
            @Field("exp") int exp,
            @Field("userLevel") int userLevel
    );

    @FormUrlEncoded
    @POST("myscore.do")
    Call<MyScoreDTO> retrieveMyScore(
            @Field("service") String service,
            @Field("userId") String userId
    );

}
