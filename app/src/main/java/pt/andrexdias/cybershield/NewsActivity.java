package pt.andrexdias.cybershield;


import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    private ListView newsListView;
    private NewsService newsService;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        newsListView = findViewById(R.id.newsListView);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsService = retrofit.create(NewsService.class);

        fetchNews();
    }

    private void fetchNews() {
        Call<NewsResponse> call = newsService.getNews("cybersecurity", "your_api_key_here");
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Article> articles = response.body().getArticles();
                    newsAdapter = new NewsAdapter(NewsActivity.this, articles);
                    newsListView.setAdapter(newsAdapter);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
