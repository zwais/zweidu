package com.bw.zweidu.util;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;
public interface ObservedApis {

    @GET
    Observable<ResponseBody> get( @Url String url);
    @POST
    Observable<ResponseBody> post( @Url String url, @QueryMap Map<String, String> parmas );
    @DELETE
    Observable<ResponseBody> delete( @Url String url );
    @PUT
    Observable<ResponseBody> put( @Url String url, @QueryMap Map<String, String> parmas );
    @POST
    @Multipart
    Observable<ResponseBody> postmap( @Url String url, @PartMap() Map<String, RequestBody> params );
    @POST
    @Multipart
    Observable<ResponseBody> postImage( @Url String url, @Part MultipartBody.Part parts );
    @POST
    @Multipart
    Observable<ResponseBody> postImageContent( @Url String url, @QueryMap Map<String, String> map, @Part MultipartBody.Part parts );
    @POST
    @Multipart
    Observable<ResponseBody> postDuoContent( @Url String url, @QueryMap Map<String, String> map, @Part MultipartBody.Part[] parts );

    @GET
    Observable<ResponseBody> shopcart_list( @Url String url,@HeaderMap Map<String,String> map);

}
