package pt.andrexdias.cybershield;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PasswordCheckActivity extends AppCompatActivity {

    private EditText password;
    private Button checkPassword;
    private TextView result;
    private PasswordCheckService passwordCheckService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_check);

        password = findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        result = findViewById(R.id.result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.pwnedpasswords.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        passwordCheckService = retrofit.create(PasswordCheckService.class);

        checkPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordStr = password.getText().toString();

                if (TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(PasswordCheckActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    checkPassword(passwordStr);
                }
            }
        });
    }

    private void checkPassword(String password) {
        String sha1Password = org.apache.commons.codec.digest.DigestUtils.sha1Hex(password);

        Call<String> call = passwordCheckService.checkPassword(sha1Password.substring(0, 5));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body();
                    if (body.contains(sha1Password.substring(5).toUpperCase())) {
                        result.setText("Password has been pwned!");
                    } else {
                        result.setText("Password is safe.");
                    }
                } else {
                    result.setText("Failed to check password.");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                result.setText("Failed to check password.");
            }
        });
    }
}