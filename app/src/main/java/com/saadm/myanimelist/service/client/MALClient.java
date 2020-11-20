package com.saadm.myanimelist.service.client;

import com.saadm.myanimelist.model.AccessToken;
import com.saadm.myanimelist.model.AnimeDetail;
import com.saadm.myanimelist.model.Data;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    Call<Data> getAnimeListData(
            @Header("Authorization") String token,
            @Query("status") String status,
            @Query("sort") String sort,
            @Query("limit") int limit,
            @Query("offset") int offset
    );

    @GET("anime")
    Call<Data> getAnimeQueryData(
            @Header("Authorization") String token,
            @Query("q") String query,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("fields") String fields
    );

    @GET("anime/{animeId}")
    Call<AnimeDetail> getAnimeData(
            @Header("Authorization") String token,
            @Path(value="animeId", encoded = true) int animeId,
            @Query("fields") String fields
    );

    @PATCH("anime/{animeId}/my_list_status")
    @FormUrlEncoded
    Call<AnimeDetail> updateList(
            @Header("Authorization") String token,
            @Path(value="animeId", encoded = true) int animeId,
            @Field("status") String status
    );

    @DELETE("anime/{animeId}/my_list_status")
    Call<Void> deleteAnimeFromList(
            @Header("Authorization") String token,
            @Path(value="animeId", encoded = true) int animeId
    );
}
