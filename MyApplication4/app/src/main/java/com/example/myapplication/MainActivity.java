package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.adapters.EstudianteAdapter;
import com.example.myapplication.database.EstudianteDatabase;
import com.example.myapplication.models.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EstudianteDatabase db;
    private EditText etNombre;
    private Button btnAgregar;
    private RecyclerView rvEstudiantes;
    private EstudianteAdapter adapter;
    private List<Estudiante> listaEstudiantes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = EstudianteDatabase.getInstancia(this);

        etNombre = findViewById(R.id.et_nombre);
        btnAgregar = findViewById(R.id.btn_agregar);
        rvEstudiantes = findViewById(R.id.rv_estudiantes);

        // Configura el RecyclerView
        rvEstudiantes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EstudianteAdapter(listaEstudiantes);
        rvEstudiantes.setAdapter(adapter);

        // Carga los estudiantes existentes en un hilo de fondo
        new Thread(() -> {
            List<Estudiante> estudiantes = db.estudianteDao().obtenerEstudiantes();
            runOnUiThread(() -> {
                listaEstudiantes.addAll(estudiantes);
                adapter.notifyDataSetChanged();
            });
        }).start();

        // Configura el botón para agregar estudiantes
        btnAgregar.setOnClickListener(view -> {
            String nombre = etNombre.getText().toString();
            if (!nombre.isEmpty()) {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(nombre);
                estudiante.setEstado("A"); // Estado predeterminado

                new Thread(() -> {
                    db.estudianteDao().insertar(estudiante);
                    List<Estudiante> estudiantesActualizados = db.estudianteDao().obtenerEstudiantes();
                    runOnUiThread(() -> {
                        listaEstudiantes.clear();
                        listaEstudiantes.addAll(estudiantesActualizados);
                        adapter.notifyDataSetChanged();
                    });
                }).start();
            }
        });
    }
}
