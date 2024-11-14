package pt.andrexdias.focus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements TaskAdapter.OnTaskClickListener {

    private RecyclerView rvUrgentTasks, rvPriorityTasks, rvNormalTasks;
    private Button btnSave, btnAddTask;

    private TaskAdapter urgentTaskAdapter, priorityTaskAdapter, normalTaskAdapter;
    private List<Task> urgentTaskList, priorityTaskList, normalTaskList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        // Infla o layout bottom_nav_view.xml
        LinearLayout bottomNavView = findViewById(R.id.bottom_nav_view);

        // Encontra os LinearLayouts dentro do bottomNavView
        LinearLayout navHome = bottomNavView.findViewById(R.id.nav_home);
        LinearLayout navTodo = bottomNavView.findViewById(R.id.nav_todo);
        LinearLayout navPomodoro = bottomNavView.findViewById(R.id.nav_pomodoro);
        LinearLayout navCalendario = bottomNavView.findViewById(R.id.nav_calendario);

        navHome.setOnClickListener(v -> {
            // Como já está na MainActivity, não precisa iniciar uma nova intent
            startActivity(new Intent(this, MainActivity.class));
            finish(); // Também não é necessário finalizar a activity aqui
        });

        navTodo.setOnClickListener(v -> {
            //startActivity(new Intent(this, ToDoListActivity.class));
           // finish();
        });

        navPomodoro.setOnClickListener(v -> {
            startActivity(new Intent(this, PomodoroActivity.class));
            finish();
        });

        navCalendario.setOnClickListener(v -> {
            startActivity(new Intent(this, CalendarioActivity.class));
            finish();
        });

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Inicializar views
        rvUrgentTasks = findViewById(R.id.rv_urgent_tasks);
        rvPriorityTasks = findViewById(R.id.rv_priority_tasks);
        rvNormalTasks = findViewById(R.id.rv_normal_tasks);
        btnSave = findViewById(R.id.btn_save);
        btnAddTask = findViewById(R.id.btn_add_task);

        // Inicializar listas de tarefas
        urgentTaskList = new ArrayList<>();
        priorityTaskList = new ArrayList<>();
        normalTaskList = new ArrayList<>();

        // Configurar adapters
        urgentTaskAdapter = new TaskAdapter(urgentTaskList, this);
        priorityTaskAdapter = new TaskAdapter(priorityTaskList, this);
        normalTaskAdapter = new TaskAdapter(normalTaskList, this);

        rvUrgentTasks.setLayoutManager(new LinearLayoutManager(this));
        rvUrgentTasks.setAdapter(urgentTaskAdapter);

        rvPriorityTasks.setLayoutManager(new LinearLayoutManager(this));
        rvPriorityTasks.setAdapter(priorityTaskAdapter);

        rvNormalTasks.setLayoutManager(new LinearLayoutManager(this));
        rvNormalTasks.setAdapter(normalTaskAdapter);

        // Carregar tarefas do Firebase
        loadTasks();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTasks();
            }
        });

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
    }

    private void loadTasks() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userTasksRef = mDatabase.child("users").child(userId).child("tasks");

        userTasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                urgentTaskList.clear();
                priorityTaskList.clear();
                normalTaskList.clear();

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    switch (task.getPriority()) {
                        case "Urgente":
                            urgentTaskList.add(task);
                            break;
                        case "Com prioridade":
                            priorityTaskList.add(task);
                            break;
                        case "Normal":
                            normalTaskList.add(task);
                            break;
                    }
                }

                urgentTaskAdapter.notifyDataSetChanged();
                priorityTaskAdapter.notifyDataSetChanged();
                normalTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("ToDoListActivity", "Erro ao carregar tarefas:", databaseError.toException());
                Toast.makeText(ToDoListActivity.this, "Erro ao carregar tarefas.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveTasks() {
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference userTasksRef = mDatabase.child("users").child(userId).child("tasks");

        // Remover tarefas concluídas do Firebase
        List<Task> tasksToRemove = new ArrayList<>();
        for (Task task : urgentTaskList) {
            if (task.isCompleted()) {
                tasksToRemove.add(task);
            }
        }
        for (Task task : tasksToRemove) {
            userTasksRef.child(task.getId()).removeValue(); // Remove a tarefa pelo ID
        }
        urgentTaskList.removeAll(tasksToRemove);

        tasksToRemove.clear();
        for (Task task : priorityTaskList) {
            if (task.isCompleted()) {
                tasksToRemove.add(task);
            }
        }
        for (Task task : tasksToRemove) {
            userTasksRef.child(task.getId()).removeValue(); // Remove a tarefa pelo ID
        }
        priorityTaskList.removeAll(tasksToRemove);

        tasksToRemove.clear();
        for (Task task : normalTaskList) {
            if (task.isCompleted()) {
                tasksToRemove.add(task);
            }
        }
        for (Task task : tasksToRemove) {
            userTasksRef.child(task.getId()).removeValue(); // Remove a tarefa pelo ID
        }
        normalTaskList.removeAll(tasksToRemove);

        // Atualizar as RecyclerViews
        urgentTaskAdapter.notifyDataSetChanged();
        priorityTaskAdapter.notifyDataSetChanged();
        normalTaskAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Tarefas salvas!", Toast.LENGTH_SHORT).show();
    }

    private void addTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Tarefa");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText inputTask = new EditText(this);
        inputTask.setHint("Tarefa");
        layout.addView(inputTask);

        final String[] priorities = {"Urgente", "Com prioridade", "Normal"};
        final int[] selectedPriority = {2}; // Índice da prioridade selecionada (inicialmente "Normal")

        builder.setSingleChoiceItems(priorities, selectedPriority[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPriority[0] = which;
            }
        });

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskDescription = inputTask.getText().toString();
                String taskPriority = priorities[selectedPriority[0]];

                Task newTask = new Task(taskDescription, taskPriority);

                // Salvar a tarefa no Firebase
                String userId = mAuth.getCurrentUser().getUid();
                DatabaseReference userTasksRef = mDatabase.child("users").child(userId).child("tasks");
                String taskId = userTasksRef.push().getKey(); // Gerar um ID único
                newTask.setId(taskId); // Definir o ID na tarefa
                userTasksRef.child(taskId).setValue(newTask); // Salvar a tarefa com o ID


                // Adicionar a tarefa à lista correta
                switch (taskPriority) {
                    case "Urgente":
                        urgentTaskList.add(newTask);
                        urgentTaskAdapter.notifyItemInserted(urgentTaskList.size() - 1);
                        break;
                    case "Com prioridade":
                        priorityTaskList.add(newTask);
                        priorityTaskAdapter.notifyItemInserted(priorityTaskList.size() - 1);
                        break;
                    case "Normal":
                        normalTaskList.add(newTask);
                        normalTaskAdapter.notifyItemInserted(normalTaskList.size() - 1);
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void onTaskClick(Task task) {
        task.setCompleted(!task.isCompleted());
        // A atualização visual é feita automaticamente pelo RecyclerView
    }
}