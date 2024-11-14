package pt.andrexdias.focus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class PomodoroActivity extends AppCompatActivity {

    public static long START_TIME_IN_MILLIS = 1500000; // 25 minutos
    public static long BREAK_TIME_IN_MILLIS = 300000; // 5 minutos

    private TextView mTextViewCountDown;
    private Button mButtonStart;
    private Button mButtonReset;
    private Button mButtonSettings;
    private TextView mTextViewStatus;
    private ProgressBar mProgressBar;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private long mEndTime;

    private boolean isWorkTime = true;

    // Notificação
    private static final String CHANNEL_ID = "pomodoro_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PomodoroActivity", "onCreate() chamado");
        setContentView(R.layout.activity_pomodoro);

        // Infla o layout bottom_nav_view.xml
        LinearLayout bottomNavView = findViewById(R.id.bottom_nav_view);

        // Encontra os LinearLayouts dentro do bottomNavView
        LinearLayout navHome = bottomNavView.findViewById(R.id.nav_home);
        LinearLayout navTodo = bottomNavView.findViewById(R.id.nav_todo);
        LinearLayout navPomodoro = bottomNavView.findViewById(R.id.nav_pomodoro);
        LinearLayout navCalendario = bottomNavView.findViewById(R.id.nav_calendario);

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        navTodo.setOnClickListener(v -> {
            startActivity(new Intent(this, ToDoListActivity.class));
            finish();
        });

        navPomodoro.setOnClickListener(v -> {
            //startActivity(new Intent(this, PomodoroActivity.class));
            //finish();
        });

        navCalendario.setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarioActivity.class));
            finish();
        });

        mTextViewCountDown = findViewById(R.id.tv_timer);
        mButtonStart = findViewById(R.id.btn_start);
        mButtonReset = findViewById(R.id.btn_reset);
        mButtonSettings = findViewById(R.id.btn_settings);
        mTextViewStatus = findViewById(R.id.tv_status);
        mProgressBar = findViewById(R.id.circularProgressBar);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PomodoroActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        loadSettings(); // Carregar as configurações ao iniciar a activity
        updateCountDownText();
    }

    private void startTimer() {
        Log.d("PomodoroActivity", "startTimer() chamado");
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d("PomodoroActivity", "onTick() chamado");
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                updateProgressBar();
            }

            @Override
            public void onFinish() {
                Log.d("PomodoroActivity", "onFinish() chamado");
                Log.d("PomodoroActivity", "isWorkTime: " + isWorkTime);
                Log.d("PomodoroActivity", "mTimeLeftInMillis: " + mTimeLeftInMillis);
                mTimerRunning = false;
                updateButtons();

                if (isWorkTime) {
                    showNotification("Vamos Descansar um bocado! Seu tempo de trabalho acabou!");
                    mTextViewStatus.setText("Vamos descansar! Está na sua pausa!");
                    mTimeLeftInMillis = BREAK_TIME_IN_MILLIS;
                } else {
                    showNotification("Vamos Trabalhar! Seu tempo de pausa acabou!");
                    mTextViewStatus.setText("Mãos na Massa! Vamos Estudar!");
                    mTimeLeftInMillis = START_TIME_IN_MILLIS;
                }

                isWorkTime = !isWorkTime;
                mTimeLeftInMillis = isWorkTime ? START_TIME_IN_MILLIS : BREAK_TIME_IN_MILLIS;
                startTimer();
            }
        }.start();

        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateButtons();
    }

    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimerRunning = false;
        loadSettings(); // Recarregar as configurações
        isWorkTime = true;
        mTextViewStatus.setText("Mãos na Massa! Vamos Estudar!");
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();
        updateButtons();
        updateProgressBar();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateProgressBar() {
        long totalTime = isWorkTime ? START_TIME_IN_MILLIS : BREAK_TIME_IN_MILLIS;
        int progress = (int) (((double) (totalTime - mTimeLeftInMillis) / totalTime) * 100);
        mProgressBar.setProgress(progress);
    }

    private void updateButtons() {
        if (mTimerRunning) {
            mButtonStart.setText("Pausar");
            mButtonReset.setVisibility(View.INVISIBLE);
        } else {
            mButtonStart.setText("Iniciar");
            mButtonReset.setVisibility(View.VISIBLE);
        }
    }

    private void showNotification(String message) {

        Intent intent = new Intent(this, PomodoroActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Pomodoro")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREFS, MODE_PRIVATE);
        START_TIME_IN_MILLIS = sharedPreferences.getLong(SettingsActivity.WORK_TIME, START_TIME_IN_MILLIS);
        BREAK_TIME_IN_MILLIS = sharedPreferences.getLong(SettingsActivity.BREAK_TIME, BREAK_TIME_IN_MILLIS);

        // Atualizar o tempo restante se o timer não estiver rodando
        if (!mTimerRunning) {
            mTimeLeftInMillis = isWorkTime ? START_TIME_IN_MILLIS : BREAK_TIME_IN_MILLIS;
            updateCountDownText();
            updateProgressBar();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("isWorkTime", isWorkTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("PomodoroActivity", "onStart() chamado");
        SharedPreferences sharedPreferences = getSharedPreferences(SettingsActivity.SHARED_PREFS, MODE_PRIVATE);

        mTimeLeftInMillis = sharedPreferences.getLong("millisLeft", START_TIME_IN_MILLIS);
        mTimerRunning = sharedPreferences.getBoolean("timerRunning", false);
        isWorkTime = sharedPreferences.getBoolean("isWorkTime", true);

        updateCountDownText();
        updateButtons();

        if (mTimerRunning) {
            mEndTime = sharedPreferences.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }
    }
}