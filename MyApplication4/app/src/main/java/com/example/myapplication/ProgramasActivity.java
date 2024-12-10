package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.ProgramaProfesionalAdapter;
import com.example.myapplication.database.EstudianteDatabase;
import com.example.myapplication.models.ProgramaProfesional;
import java.util.ArrayList;
import java.util.List;

public class ProgramasActivity extends AppCompatActivity {
    private EstudianteDatabase db;
    private EditText etNombrePrograma;
    private Button btnAgregarPrograma;
    private RecyclerView rvProgramas;
    private ProgramaProfesionalAdapter adapter;
    private List<ProgramaProfesional> listaProgramas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        db = EstudianteDatabase.getInstancia(this);

        etNombrePrograma = findViewById(R.id.et_programa_nombre);
        btnAgregarPrograma = findViewById(R.id.btn_agregar_programa);
        rvProgramas = findViewById(R.id.rv_programas);

        rvProgramas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProgramaProfesionalAdapter(listaProgramas);
        rvProgramas.setAdapter(adapter);

        cargarProgramas();

        btnAgregarPrograma.setOnClickListener(view -> {
            String nombre = etNombrePrograma.getText().toString();
            if (!nombre.isEmpty()) {
                ProgramaProfesional programa = new ProgramaProfesional();
                programa.setNombre(nombre);
                programa.setEstado("A");

                new Thread(() -> {
                    db.programaProfesionalDao().insertar(programa);
                    cargarProgramas();
                }).start();
            }
        });
    }

    private void cargarProgramas() {
        new Thread(() -> {
            List<ProgramaProfesional> programas = db.programaProfesionalDao().obtenerTodos();
            runOnUiThread(() -> {
                listaProgramas.clear();
                listaProgramas.addAll(programas);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}
