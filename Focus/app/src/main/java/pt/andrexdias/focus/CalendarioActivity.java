package pt.andrexdias.focus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import pt.andrexdias.focus.EventAdapter.OnItemClickListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarioActivity extends AppCompatActivity implements OnItemClickListener {

    private CalendarView calendarView;
    private RecyclerView recyclerViewEvents;
    private FloatingActionButton fabAddEvent;
    private EditText editTextDate; // Declarar editTextDate como atributo

    private DatabaseReference eventsRef;
    private FirebaseAuth mAuth;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        // Infla o layout bottom_nav_view.xml
        LinearLayout bottomNavView = findViewById(R.id.bottom_nav_view);

        // Encontra os LinearLayouts dentro do bottomNavView
        LinearLayout navHome = bottomNavView.findViewById(R.id.nav_home);
        LinearLayout navTodo = bottomNavView.findViewById(R.id.nav_todo);
        LinearLayout navPomodoro = bottomNavView.findViewById(R.id.nav_pomodoro);
        LinearLayout navCalendario = bottomNavView.findViewById(R.id.nav_calendario);

        navHome.setOnClickListener(v -> {
             startActivity(new Intent(this, MainActivity.class));
            finish(); // Também não é necessário finalizar a activity aqui
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
            //startActivity(new Intent(this, CalendarioActivity.class));
            //finish();
        });

        calendarView = findViewById(R.id.calendarView);
        recyclerViewEvents = findViewById(R.id.recyclerViewEvents);
        fabAddEvent = findViewById(R.id.fabAddEvent);
        editTextDate = findViewById(R.id.editTextDate); // Inicializar editTextDate

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            eventsRef = FirebaseDatabase.getInstance().getReference("events").child(user.getUid());
        } else {
            // Lidar com caso em que o usuário não está logado
            Toast.makeText(this, "Usuário não logado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(this));
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList, this); // Passar "this" como listener
        recyclerViewEvents.setAdapter(eventAdapter);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                loadEvents(year, month, dayOfMonth);
            }
        });

        fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEventDialog();
            }
        });
    }

    private void showAddEventDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        final EditText editTextName = dialogView.findViewById(R.id.editTextEventName);
        final EditText editTextLocal = dialogView.findViewById(R.id.editTextLocal);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextDate);
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(editTextTime);
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button btnSaveEvent = dialogView.findViewById(R.id.btnSaveEvent);
        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String local = editTextLocal.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();

                if (name.isEmpty() || local.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(CalendarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                addEvent(name, local, date, time);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog(final EditText editTextDate) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String dateString = DateFormat.format("dd/MM/yyyy", calendar).toString();
                        editTextDate.setText(dateString);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(final EditText editTextTime) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        String timeString = DateFormat.format("HH:mm", calendar).toString();
                        editTextTime.setText(timeString);
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void addEvent(String name, String local, String date, String time) {
        String eventId = eventsRef.push().getKey();
        Event event = new Event(eventId, name, local, date, time);

        eventsRef.child(eventId).setValue(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Firebase", "Event added successfully!");
                            Toast.makeText(CalendarioActivity.this, "Evento adicionado com sucesso!", Toast.LENGTH_SHORT).show();

                            try {
                                String dateString = editTextDate.getText() != null ? editTextDate.getText().toString() : "";
                                String[] dateParts = dateString.split("/");
                                int dayOfMonth = Integer.parseInt(dateParts[0]);
                                int month = Integer.parseInt(dateParts[1]) - 1;
                                int year = Integer.parseInt(dateParts[2]);

                                loadEvents(year, month, dayOfMonth);
                            } catch (NumberFormatException e) {
                                Log.e("Firebase", "Erro ao converter data: " + e.getMessage());
                                Toast.makeText(CalendarioActivity.this, "Erro ao converter data.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Log.e("Firebase", "Error adding event: " + task.getException());
                            Toast.makeText(CalendarioActivity.this, "Erro ao adicionar evento.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadEvents(int year, int month, int dayOfMonth) {
        String dateString = String.format("%02d/%02d/%04d", dayOfMonth, month + 1,year);
        Query query = eventsRef.orderByChild("date").equalTo(dateString);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CalendarioActivity.this, "Erro ao carregar eventos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(Event event) {
        showEventOptionsDialog(event);
    }

    private void showEventOptionsDialog(final Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opções do Evento");
        builder.setItems(new CharSequence[]{"Editar", "Eliminar"}, (dialog, which) -> {
            if (which == 0) {
                showEditEventDialog(event);
            } else if (which == 1) {
                deleteEvent(event);
            }
        });
        builder.show();
    }

    private void showEditEventDialog(final Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        final EditText editTextName = dialogView.findViewById(R.id.editTextEventName);
        final EditText editTextLocal = dialogView.findViewById(R.id.editTextLocal);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);

        // Preencher os campos com os dados do evento
        editTextName.setText(event.getName());
        editTextLocal.setText(event.getLocal());
        editTextDate.setText(event.getDate());
        editTextTime.setText(event.getTime());

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editTextDate);
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(editTextTime);
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button btnSaveEvent = dialogView.findViewById(R.id.btnSaveEvent);
        btnSaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String local = editTextLocal.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();

                if (name.isEmpty() || local.isEmpty() || date.isEmpty() || time.isEmpty()) {
                    Toast.makeText(CalendarioActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                updateEvent(event.getId(), name, local, date, time);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateEvent(String eventId, String name, String local, String date, String time) {
        Event updatedEvent = new Event(eventId, name, local, date, time);
        eventsRef.child(eventId).setValue(updatedEvent)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CalendarioActivity.this, "Evento atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        loadEventsFromDate(date);
                    } else {
                        Toast.makeText(CalendarioActivity.this, "Erro ao atualizar evento.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteEvent(Event event) {
        eventsRef.child(event.getId()).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CalendarioActivity.this, "Evento eliminado com sucesso!", Toast.LENGTH_SHORT).show();
                        loadEventsFromDate(event.getDate());
                    } else {
                        Toast.makeText(CalendarioActivity.this, "Erro ao eliminar evento.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadEventsFromDate(String date) {
        try {
            String[] dateParts = date.split("/");
            int dayOfMonth = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Ajustar o mês para o índice do Calendar (0-11)
            int year = Integer.parseInt(dateParts[2]);
            loadEvents(year, month, dayOfMonth);
        } catch (NumberFormatException e) {
            Log.e("Firebase", "Erro ao converter data: " + e.getMessage());
            Toast.makeText(CalendarioActivity.this, "Erro ao converter data.", Toast.LENGTH_SHORT).show();
        }
    }
}