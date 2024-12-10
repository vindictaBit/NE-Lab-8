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

import com.example.myapplication.adapters.ProgramaProfesionalAdapter;
import com.example.myapplication.database.EstudianteDatabase;
import com.example.myapplication.models.ProgramaProfesional;

import java.util.ArrayList;
import java.util.List;

public class ProgramasActivity extends AppCompatActivity {
    private RecyclerView rvProgramas;
    private ProgramaProfesionalAdapter adapter;
    private List<ProgramaProfesional> listaProgramas = new ArrayList<>();
    private EstudianteDatabase db;
    private Button btnGuardarPrograma;
    private EditText etNombrePrograma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programas);

        db = EstudianteDatabase.getInstancia(this);

        rvProgramas = findViewById(R.id.rv_programas);
        rvProgramas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProgramaProfesionalAdapter(listaProgramas, this::mostrarOpcionesPrograma);
        rvProgramas.setAdapter(adapter);

        etNombrePrograma = findViewById(R.id.et_nombre_programa);
        btnGuardarPrograma = findViewById(R.id.btn_guardar_programa);

        cargarProgramas();

        btnGuardarPrograma.setOnClickListener(v -> guardarPrograma());

        SearchView searchView = findViewById(R.id.searchViewProgramas);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarProgramas(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarProgramas(newText);
                return true;
            }
        });

        Spinner spinnerOrden = findViewById(R.id.spinnerOrdenProgramas);
        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarProgramasConOrden(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
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

    private void buscarProgramas(String query) {
        new Thread(() -> {
            List<ProgramaProfesional> programas = db.programaProfesionalDao().buscarPorNombre("%" + query + "%");
            runOnUiThread(() -> {
                listaProgramas.clear();
                listaProgramas.addAll(programas);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void cargarProgramasConOrden(int orden) {
        new Thread(() -> {
            List<ProgramaProfesional> programas = orden == 0 ?
                    db.programaProfesionalDao().ordenarPorNombre() :
                    db.programaProfesionalDao().ordenarPorCodigo();
            runOnUiThread(() -> {
                listaProgramas.clear();
                listaProgramas.addAll(programas);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void guardarPrograma() {
        String nombre = etNombrePrograma.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Por favor ingrese un nombre", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgramaProfesional programa = new ProgramaProfesional();
        programa.setNombre(nombre);

        new Thread(() -> {
            db.programaProfesionalDao().insertar(programa);
            runOnUiThread(() -> {
                Toast.makeText(this, "Programa agregado", Toast.LENGTH_SHORT).show();
                cargarProgramas();
                etNombrePrograma.setText("");  // Limpiar el campo de texto
            });
        }).start();
    }

    private void mostrarOpcionesPrograma(ProgramaProfesional programa) {
        String[] opciones = {"Modificar", "Eliminar Lógicamente", "Inactivar", "Reactivar"};
        new AlertDialog.Builder(this)
                .setTitle("Opciones para el Programa")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Modificar
                            mostrarDialogoModificar(programa);
                            break;
                        case 1: // Eliminar Lógicamente
                            actualizarEstadoPrograma(programa, "*");
                            break;
                        case 2: // Inactivar
                            actualizarEstadoPrograma(programa, "I");
                            break;
                        case 3: // Reactivar
                            actualizarEstadoPrograma(programa, "A");
                            break;
                    }
                })
                .show();
    }

    private void mostrarDialogoModificar(ProgramaProfesional programa) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Programa");

        View view = getLayoutInflater().inflate(R.layout.dialogo_modificar_programa, null);
        EditText etNombre = view.findViewById(R.id.etNombrePrograma);

        etNombre.setText(programa.getNombre());

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            programa.setNombre(etNombre.getText().toString());
            new Thread(() -> {
                db.programaProfesionalDao().actualizar(programa);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Programa actualizado", Toast.LENGTH_SHORT).show();
                    cargarProgramas();
                });
            }).start();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void actualizarEstadoPrograma(ProgramaProfesional programa, String nuevoEstado) {
        programa.setEstado(nuevoEstado);
        new Thread(() -> {
            db.programaProfesionalDao().actualizar(programa);
            runOnUiThread(this::cargarProgramas);
        }).start();
    }
}