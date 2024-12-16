package pt.andrexdias.cybershield;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("everything")
    Call<NewsResponse> getNews(@Query("q") String query, @Query("apiKey") String apiKey);
}
