package com.example.tefah.quran;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by tefah on 25/04/18.
 */

public interface FileUploadService {
    @Multipart
    @POST("/")
    Call<ResponseBody> upload(
            @Part MultipartBody.Part file
    );
    @GET("/")
    Call<ResponseBody> getHtml();
}
//description might be used later
//    @Part("description") RequestBody description,
