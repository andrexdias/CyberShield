package pt.andrexdias.focus;

import static pt.andrexdias.focus.PomodoroActivity.BREAK_TIME_IN_MILLIS;
import static pt.andrexdias.focus.PomodoroActivity.START_TIME_IN_MILLIS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText mEditTextWorkTime;
    private EditText mEditTextBreakTime;
    private Button mButtonSave;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String WORK_TIME = "workTime";
    public static final String BREAK_TIME = "breakTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mEditTextWorkTime = findViewById(R.id.edit_text_work_time);
        mEditTextBreakTime = findViewById(R.id.edit_text_break_time);
        mButtonSave = findViewById(R.id.button_save);

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                finish(); // Fecha a activity de Ajustes
            }
        });

        loadSettings();
    }

    private void saveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Obter os valores dos campos de entrada (em minutos)
        int workTime = Integer.parseInt(mEditTextWorkTime.getText().toString());
        int breakTime = Integer.parseInt(mEditTextBreakTime.getText().toString());

        // Converter para milissegundos e salvar
        editor.putLong(WORK_TIME, workTime * 60000L);
        editor.putLong(BREAK_TIME, breakTime * 60000L);

        editor.apply();
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        long workTime = sharedPreferences.getLong(WORK_TIME, START_TIME_IN_MILLIS);
        long breakTime = sharedPreferences.getLong(BREAK_TIME, BREAK_TIME_IN_MILLIS);

        // Converter para minutos e exibir nos campos de entrada
        mEditTextWorkTime.setText(String.valueOf(workTime / 60000));
        mEditTextBreakTime.setText(String.valueOf(breakTime / 60000));
    }
}