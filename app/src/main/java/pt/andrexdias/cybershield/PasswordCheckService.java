package pt.andrexdias.cybershield;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PasswordCheckService {
    @GET("range/{hashPrefix}")
    Call<String> checkPassword(@Path("hashPrefix") String hashPrefix);
}