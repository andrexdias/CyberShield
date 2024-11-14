package pt.andrexdias.focus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import pt.andrexdias.focus.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private TextView fraseMotivacionalTextView;
    private List<String> frasesMotivacionais = new ArrayList<>(); // <-- Lista de frases aqui


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    // Infla o layout bottom_nav_view.xml
        LinearLayout bottomNavView = findViewById(R.id.bottom_nav_view);

        // Encontra os LinearLayouts dentro do bottomNavView
        LinearLayout navHome = bottomNavView.findViewById(R.id.nav_home);
        LinearLayout navTodo = bottomNavView.findViewById(R.id.nav_todo);
        LinearLayout navPomodoro = bottomNavView.findViewById(R.id.nav_pomodoro);
        LinearLayout navCalendario = bottomNavView.findViewById(R.id.nav_calendario);

        navHome.setOnClickListener(v -> {
            // Como já está na MainActivity, não precisa iniciar uma nova intent
            // startActivity(new Intent(this, MainActivity.class));
            // finish(); // Também não é necessário finalizar a activity aqui
        });

        navTodo.setOnClickListener(v -> {
            startActivity(new Intent(this, ToDoListActivity.class));
            finish();
        });

        navPomodoro.setOnClickListener(v -> {
            startActivity(new Intent(this, PomodoroActivity.class));
            finish();
        });

        navCalendario.setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarioActivity.class));
            finish();
        });

        // Inicializar a lista de frases motivacionais
        frasesMotivacionais.add("Acredite em si mesmo e tudo será possível.");
        frasesMotivacionais.add("O sucesso é a soma de pequenos esforços repetidos dia após dia.");
        frasesMotivacionais.add("A persistência é o caminho do êxito.");
        frasesMotivacionais.add("Não tenha medo de falhar, tenha medo de não tentar.");
        // Adicione mais frases aqui...

        fraseMotivacionalTextView = findViewById(R.id.frase_motivacional_textview);

        exibirFraseMotivacional();

        Button gerarFraseButton = findViewById(R.id.gerar_frase_button);
        if (gerarFraseButton != null) {
            gerarFraseButton.setOnClickListener(v -> exibirFraseMotivacional());
        }
    }

    private String gerarFraseMotivacional() {
        Random random = new Random();
        int indiceAleatorio = random.nextInt(frasesMotivacionais.size());
        return frasesMotivacionais.get(indiceAleatorio);
    }

    private void exibirFraseMotivacional() {
        String frase = gerarFraseMotivacional();
        fraseMotivacionalTextView.setText(frase);
    }

}


