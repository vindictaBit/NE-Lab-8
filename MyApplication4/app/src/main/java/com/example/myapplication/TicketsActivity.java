package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapters.TicketEntregaAdapter;
import com.example.myapplication.database.EstudianteDatabase;
import com.example.myapplication.models.Estudiante;
import com.example.myapplication.models.ProgramaProfesional;
import com.example.myapplication.models.TicketEntrega;

import java.util.ArrayList;
import java.util.List;

public class TicketsActivity extends AppCompatActivity {
    private EditText etNumeroTicket, etFecha, etDescripcion;
    private Spinner spEstudiante, spPrograma;
    private Button btnGuardarTicket;
    private RecyclerView rvTickets;
    private TicketEntregaAdapter ticketAdapter;

    private EstudianteDatabase db;
    private List<TicketEntrega> listaTickets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);

        // Vincular vistas
        etNumeroTicket = findViewById(R.id.et_numero_ticket);
        etFecha = findViewById(R.id.et_fecha);
        etDescripcion = findViewById(R.id.et_descripcion);
        spEstudiante = findViewById(R.id.sp_estudiante);
        spPrograma = findViewById(R.id.sp_programa);
        btnGuardarTicket = findViewById(R.id.btn_guardar_ticket);
        rvTickets = findViewById(R.id.rv_tickets);

        // Inicializar base de datos
        db = EstudianteDatabase.getInstancia(this);

        // Configurar RecyclerView
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        ticketAdapter = new TicketEntregaAdapter(listaTickets, this::mostrarOpcionesTicket);
        rvTickets.setAdapter(ticketAdapter);

        // Cargar datos en Spinners y lista de tickets
        cargarSpinners();
        cargarTickets();

        // Configurar botón para guardar ticket
        btnGuardarTicket.setOnClickListener(v -> guardarTicket());
    }

    private void cargarSpinners() {
        new Thread(() -> {
            List<Estudiante> estudiantes = db.estudianteDao().obtenerActivos();
            List<ProgramaProfesional> programas = db.programaProfesionalDao().obtenerActivos();

            runOnUiThread(() -> {
                ArrayAdapter<Estudiante> estudianteAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, estudiantes);
                estudianteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEstudiante.setAdapter(estudianteAdapter);

                ArrayAdapter<ProgramaProfesional> programaAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, programas);
                programaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPrograma.setAdapter(programaAdapter);
            });
        }).start();
    }

    private void guardarTicket() {
        String numeroTicket = etNumeroTicket.getText().toString();
        String fecha = etFecha.getText().toString();
        String descripcion = etDescripcion.getText().toString();
        Estudiante estudianteSeleccionado = (Estudiante) spEstudiante.getSelectedItem();
        ProgramaProfesional programaSeleccionado = (ProgramaProfesional) spPrograma.getSelectedItem();

        if (numeroTicket.isEmpty() || fecha.isEmpty() || descripcion.isEmpty() || estudianteSeleccionado == null || programaSeleccionado == null) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        TicketEntrega nuevoTicket = new TicketEntrega();
        nuevoTicket.setNumeroTicket(numeroTicket);
        nuevoTicket.setFecha(fecha);
        nuevoTicket.setDescripcion(descripcion);
        nuevoTicket.setEstudianteId(estudianteSeleccionado.getId());
        nuevoTicket.setProgramaId(programaSeleccionado.getId());

        new Thread(() -> {
            db.ticketEntregaDao().insertar(nuevoTicket);
            runOnUiThread(() -> {
                Toast.makeText(this, "Ticket guardado", Toast.LENGTH_SHORT).show();
                cargarTickets();
                limpiarCampos();
            });
        }).start();
    }

    private void cargarTickets() {
        new Thread(() -> {
            List<TicketEntrega> tickets = db.ticketEntregaDao().obtenerTodos();
            runOnUiThread(() -> {
                listaTickets.clear();
                listaTickets.addAll(tickets);
                ticketAdapter.notifyDataSetChanged();
            });
        }).start();
    }

    private void limpiarCampos() {
        etNumeroTicket.setText("");
        etFecha.setText("");
        etDescripcion.setText("");
        spEstudiante.setSelection(0);
        spPrograma.setSelection(0);
    }

    private void mostrarOpcionesTicket(TicketEntrega ticket) {
        String[] opciones = {"Modificar", "Eliminar Lógicamente", "Inactivar", "Reactivar"};
        new AlertDialog.Builder(this)
                .setTitle("Opciones para el Ticket")
                .setItems(opciones, (dialog, which) -> {
                    switch (which) {
                        case 0: // Modificar
                            mostrarDialogoModificar(ticket);
                            break;
                        case 1: // Eliminar Lógicamente
                            actualizarEstadoTicket(ticket, "*");
                            break;
                        case 2: // Inactivar
                            actualizarEstadoTicket(ticket, "I");
                            break;
                        case 3: // Reactivar
                            actualizarEstadoTicket(ticket, "A");
                            break;
                    }
                })
                .show();
    }

    private void mostrarDialogoModificar(TicketEntrega ticket) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar Ticket");

        // Formulario en el diálogo
        View view = getLayoutInflater().inflate(R.layout.dialogo_modificar_ticket, null);
        EditText etNumero = view.findViewById(R.id.et_numero_ticket);
        EditText etFechaMod = view.findViewById(R.id.et_fecha);
        EditText etDescripcionMod = view.findViewById(R.id.et_descripcion);

        etNumero.setText(ticket.getNumeroTicket());
        etFechaMod.setText(ticket.getFecha());
        etDescripcionMod.setText(ticket.getDescripcion());

        builder.setView(view);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            ticket.setNumeroTicket(etNumero.getText().toString());
            ticket.setFecha(etFechaMod.getText().toString());
            ticket.setDescripcion(etDescripcionMod.getText().toString());

            new Thread(() -> {
                db.ticketEntregaDao().actualizar(ticket);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ticket actualizado", Toast.LENGTH_SHORT).show();
                    cargarTickets();
                });
            }).start();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void actualizarEstadoTicket(TicketEntrega ticket, String nuevoEstado) {
        ticket.setEstado(nuevoEstado);
        new Thread(() -> {
            db.ticketEntregaDao().actualizar(ticket);
            runOnUiThread(() -> cargarTickets());
        }).start();
    }
}
