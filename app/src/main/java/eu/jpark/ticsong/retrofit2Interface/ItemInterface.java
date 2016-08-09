package eu.jpark.ticsong.retrofit2Interface;

import eu.jpark.ticsong.DTO.ItemDTO;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Daesub Kim on 2016-07-31.
 */
public interface ItemInterface {

    @FormUrlEncoded
    @POST("item.do")
    Call<ItemDTO> getItem(
            @Field("service") String service,
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("item.do")
    Call<ItemDTO> insertDefaultItem(
            @Field("service") String service,
            @Field("userId") String userId,
            @Field("item1Cnt") int item1Cnt,
            @Field("item2Cnt") int item2Cnt,
            @Field("item3Cnt") int item3Cnt,
            @Field("item4Cnt") int item4Cnt
    );

    @FormUrlEncoded
    @POST("item.do")
    Call<ItemDTO> insertItem(
            @Field("service") String service,
            @Field("userId") String userId,
            @Field("item1Cnt") int item1Cnt,
            @Field("item2Cnt") int item2Cnt,
            @Field("item3Cnt") int item3Cnt,
            @Field("item4Cnt") int item4Cnt
    );

    @FormUrlEncoded
    @POST("item.do")
    Call<ItemDTO> updateItem(
            @Field("service") String service,
            @Field("userId") String userId,
            @Field("item1Cnt") int item1Cnt,
            @Field("item2Cnt") int item2Cnt,
            @Field("item3Cnt") int item3Cnt,
            @Field("item4Cnt") int item4Cnt
    );

    @FormUrlEncoded
    @POST("item.do")
    Call<ItemDTO> retrieveItem(
            @Field("service") String service,
            @Field("userId") String userId
    );






}