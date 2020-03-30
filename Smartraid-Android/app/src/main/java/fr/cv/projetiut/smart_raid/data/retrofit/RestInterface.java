package fr.cv.projetiut.smart_raid.data.retrofit;

import java.util.List;

import fr.cv.projetiut.smart_raid.data.Course;
import fr.cv.projetiut.smart_raid.data.CourseType;
import fr.cv.projetiut.smart_raid.data.Message;
import fr.cv.projetiut.smart_raid.data.Photo;
import fr.cv.projetiut.smart_raid.data.Position;
import fr.cv.projetiut.smart_raid.data.ServerResponce;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by valentincroset on November,2019
 * Project : Smart-raid
 * IUT de Roanne
 * Team : Léo - Valentin - Bastien - Alex - Khadidia
 */
public interface RestInterface {

    @GET("/")
    Call<String> test();

    @GET("api/list/courses")
    Call<List<CourseType>> coursesList();

    @GET("api/profile/{id}/positions")
    Call<List<Position>> userPositions(@Path("id") int courseId);

    @GET("api/profile/{id}/messages")
    Call<List<Message>> userMessages(@Path("id") int courseId);

    @GET("api/profile/{id}/photos")
    Call<List<Photo>> userPhotos(@Path("id") int courseId);

    @GET("api/profile/{id}/courses")
    Call<List<Course>> userCourses(@Path("id") int userId);

    @GET("api/profile/{id}/all")
    Call<List<LoginResponce>> autologin(@Path("id") int id);

    @POST("api/register")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("email") String email,
                                    @Field("name") String name,
                                    @Field("surname") String surname,
                                    @Field("pass") String pass);

    @POST("api/login")
    @FormUrlEncoded
    Call<LoginResponce> loginUser(@Field("email") String email,
                                  @Field("password") String password);

    @POST("api/position")
    @FormUrlEncoded
    Call<String> position(@Field("id") int id,
                          @Field("latitude") Double latitude,
                          @Field("longitude") Double longitude);

    @POST("api/insert/course")
    @FormUrlEncoded
    Call<String> course(@Field("id") int id,
                        @Field("idcourse") int idcourse);

    @POST("api/insert/message")
    @FormUrlEncoded
    Call<String> sendMessage(@Field("id") int id,
                             @Field("message") String message);

    @Multipart
    @POST("api/insert/image")
    Call<String> uploadImage(@Part MultipartBody.Part file, @Part("file") RequestBody name);

}
