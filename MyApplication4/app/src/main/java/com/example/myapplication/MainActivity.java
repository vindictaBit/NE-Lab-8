package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnEstudiantes, btnProgramas, btnTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular botones con sus IDs
        btnEstudiantes = findViewById(R.id.btnEstudiantes);
        btnProgramas = findViewById(R.id.btnProgramas);
        btnTickets = findViewById(R.id.btnTickets);

        // Configurar los eventos de clic para navegar a cada actividad
        btnEstudiantes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EstudiantesActivity.class);
            startActivity(intent);
        });

        btnProgramas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgramasActivity.class);
            startActivity(intent);
        });

        btnTickets.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TicketsActivity.class);
            startActivity(intent);
        });
    }
}
