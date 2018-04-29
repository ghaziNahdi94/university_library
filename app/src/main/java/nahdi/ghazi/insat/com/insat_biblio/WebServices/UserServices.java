package nahdi.ghazi.insat.com.insat_biblio.WebServices;

import java.util.List;

import nahdi.ghazi.insat.com.insat_biblio.MyObjects.Emprunt;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.ResultCnx;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.User;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmail;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserEmailPassword;
import nahdi.ghazi.insat.com.insat_biblio.MyObjects.UserGet;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by ghazi on 04/04/2018.
 */

public interface UserServices {


    @PUT("/user")
    Call<User> createUser(@Body User user);


    @Multipart
    @POST("/card")
    Call<ResponseBody> sendImage(@Part MultipartBody.Part img);


    @POST("/email")
    Call<ResponseBody> getUserByEmail(@Body UserEmail email);

    @POST("/email/password")
    Call<ResultCnx> getUserByEmailPassword(@Body UserEmailPassword emailPassword);


    @PUT("/borrow")
    Call<ResponseBody> createBorrow(@Body Emprunt emprunt);


    @POST("/state")
    Call<UserGet> getUser(@Body UserEmail userEmail);


}
