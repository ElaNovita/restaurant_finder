package com.example.novita.ela.restaurant.helper;


import com.example.novita.ela.restaurant.Model.CafeModel;
import com.example.novita.ela.restaurant.Model.GalleryModel;
import com.example.novita.ela.restaurant.Model.LikeModel;
import com.example.novita.ela.restaurant.Model.MarkModel;
import com.example.novita.ela.restaurant.Model.MenuModel;
import com.example.novita.ela.restaurant.Model.RegisterModel;
import com.example.novita.ela.restaurant.Model.ReservasiModel;
import com.example.novita.ela.restaurant.Model.Success;
import com.example.novita.ela.restaurant.Model.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by elaa on 02/04/17.
 */

public interface MyInterface  {

    @GET("cafe_list.php")
    Call<List<CafeModel>> getCafeList();

    @GET("cafe_list_likes.php")
    Call<List<CafeModel>> getCafeListLikes();

    @GET("cafe_list_alpha.php")
    Call<List<CafeModel>> getCafeListAlpha();

    @GET("cafe_detail.php")
    Call<CafeModel> getCafeDetail(@Query("id") int id);

    @GET("menu.php")
    Call<List<MenuModel>> getMenuList(@Query("id") int id);

    @GET("gallery.php")
    Call<List<GalleryModel>> getPhotoList(@Query("id") int id);

    @GET("myreservation.php")
    Call<List<ReservasiModel>> getMyReservationList(@Query("id") int id);

    @FormUrlEncoded
    @POST("login.php")
    Call<UserModel> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterModel> register(@Field("username") String username, @Field("password") String password,
                                 @Field("email") String email, @Field("gambar") String gambar);
    @FormUrlEncoded
    @POST("reservasi.php")
    Call<ReservasiModel> reservasi(@Field("meja") int meja, @Field("keterangan") String ket,
                                   @Field("tanggal") String tgl, @Field("jam") String jam,
                                   @Field("cafe_id") int cafe_id, @Field("pelanggan_id") int pelanggan_id,
                                   @Field("pelanggan_user_id") int pelanggan_user_id);

    @Multipart
    @POST("edit.php")
    Call<UserModel> editProfile(@Part("id") RequestBody id,
                                @Part("username") RequestBody username,
                                @Part("password") RequestBody password,
                                @Part("email") RequestBody email,
                                @Part MultipartBody.Part gambar);

    @FormUrlEncoded
    @POST("likes.php")
    Call<Success> like(@Field("cafe_id") int cafe_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("set_rating.php")
    Call<CafeModel> setRating(@Field("id") int id,
                            @Field("rating") Double rating);

    @FormUrlEncoded
    @POST("check_like.php")
    Call<LikeModel> cekLike(@Field("cafe_id") int cafe_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("register_id.php")
    Call<UserModel> sendToken(@Field("id") int id, @Field("reg_id") String reg_id);

    @FormUrlEncoded
    @POST("user_detail.php")
    Call<UserModel> userDetail(@Field("id") int id);

    @FormUrlEncoded
    @POST("search.php")
    Call<CafeModel> search(@Field("search") String search);

    @FormUrlEncoded
    @POST("delete_token.php")
    Call<UserModel> deleteToken(@Field("id") int id);

    @FormUrlEncoded
    @POST("cek_marked.php")
    Call<MarkModel> cekMarked(@Field("cafe_id") int cafe_id,
                              @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("marked.php")
    Call<CafeModel> mark(@Field("cafe_id") int cafe_id,
                         @Field("user_id") int user_id);

    @Multipart
    @POST("upload_photo.php")
    Call<GalleryModel> uploadPhoto(@Part("cafe_id") RequestBody cafe_id,
                                   @Part MultipartBody.Part gambar);
}
