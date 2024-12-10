package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.EstudianteAdapter;
import com.example.myapplication.database.EstudianteDatabase;
import com.example.myapplication.models.Estudiante;

import java.util.ArrayList;
import java.util.List;

public class EstudiantesActivity extends AppCompatActivity {
    private RecyclerView rvEstudiantes;
    private EstudianteAdapter adapter;
    private List<Estudiante> listaEstudiantes = new ArrayList<>();
    private EstudianteDatabase db;
    private Button btnGuardar;
    private EditText etNombre; // Declare the EditText variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);

        db = EstudianteDatabase.getInstancia(this);

        rvEstudiantes = findViewById(R.id.rv_estudiantes);
        rvEstudiantes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EstudianteAdapter(listaEstudiantes, this::mostrarOpcionesEstudiante);
        rvEstudiantes.setAdapter(adapter);

        etNombre = findViewById(R.id.et_nombre); // Initialize the EditText variable
        btnGuardar = findViewById(R.id.btn_guardar); // Initialize the Button variable

        cargarEstudiantes();

        btnGuardar.setOnClickListener(v -> guardarEstudiante());

        SearchView searchView = findViewById(R.id.searchViewEstudiantes);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarEstudiantes(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarEstudiantes(newText);
                return true;
            }
        });

        Spinner spinnerOrden = findViewById(R.id.spinnerOrdenEstudiantes);
        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarEstudiantesConOrden(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcional: Manejo si no se selecciona nada
            }
        });
    }

    private void cargarEstudiantes() {
        new Thread(() -> {
            List<Estudiante> estudiantes = db.estudianteDao().obtenerEstudiantes();
            runOnUiThread(() -> {
                listaEstudiantes.clear();
                listaEstudiantes.addAll(estudiantes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void buscarEstudiantes(String query) {
        new Thread(() -> {
            List<Estudiante> estudiantes = db.estudianteDao().buscarPorNombre("%" + query + "%");
            runOnUiThread(() -> {
                listaEstudiantes.clear();
                listaEstudiantes.addAll(estudiantes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void cargarEstudiantesConOrden(int orden) {
        new Thread(() -> {
            List<Estudiante> estudiantes = orden == 0 ?
                    db.estudianteDao().ordenarPorNombre() :
                    db.estudianteDao().ordenarPorCodigo();
            runOnUiThread(() -> {
                listaEstudiantes.clear();
                listaEstudiantes.addAll(estudiantes);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void guardarEstudiante() {
        String nombre = etNombre.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setNombre(nombre);

        new Thread(() -> {
            db.estudianteDao().insertar(estudiante);
            runOnUiThread(() -> {
                Toast.makeText(this, "Estudiante agregado", Toast.LENGTH_SHORT).show();
                cargarEstudiantes();
                etNombre.setText("");  // Limpiar el campo de texto
            });
        }).start();
    }

    private void mostrarOpcionesEstudiante(Estudiante estudiante) {
        String[] opciones = {"Modificar", "Eliminar Lógicamente", "Inactivar", "Reactivar"};
        new AlertDialog.Builder(this)
                .setTitle("Opciones para el Estudiante")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Modificar
                            mostrarDialogoModificar(estudiante);
                            break;
                        case 1: // Eliminar Lógicamente
                            actualizarEstadoEstudiante(estudiante, "*");
                            break;
                        case 2: // Inactivar
                            actualizarEstadoEstudiante(estudiante, "I");
                            break;
                        case 3: // Reactivar
                            actualizarEstadoEstudiante(estudiante, "A");
                            break;
                    }
                })
                .show();
    }

    private void mostrarDialogoModificar(Estudiante estudiante) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Estudiante");

        View view = getLayoutInflater().inflate(R.layout.dialogo_modificar_estudiante, null);
        EditText etNombre = view.findViewById(R.id.etNombreEstudiante);

        etNombre.setText(estudiante.getNombre());

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            estudiante.setNombre(etNombre.getText().toString());
            new Thread(() -> {
                db.estudianteDao().actualizar(estudiante);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show();
                    cargarEstudiantes();
                });
            }).start();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void actualizarEstadoEstudiante(Estudiante estudiante, String nuevoEstado) {
        estudiante.setEstado(nuevoEstado);
        new Thread(() -> {
            db.estudianteDao().actualizar(estudiante);
            runOnUiThread(this::cargarEstudiantes);
        }).start();
    }
}