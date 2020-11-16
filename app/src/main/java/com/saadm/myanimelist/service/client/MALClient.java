package com.saadm.myanimelist.service.client;

import com.saadm.myanimelist.model.AccessToken;
import com.saadm.myanimelist.model.Data;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MALClient {

    @Headers("Accept: application/json")
    @POST("token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("code") String code,
            @Field("code_verifier") String verifier,
            @Field("grant_type") String grantType
    );

    @GET("users/@me/animelist")
    Call<Data> getAnimeListData();
}
