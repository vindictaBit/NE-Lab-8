package com.example.myapplication.database;

import android.content.Context;

import androidx.room.*;
import com.example.myapplication.dao.EstudianteDao;
import com.example.myapplication.dao.ProgramaProfesionalDao;
import com.example.myapplication.dao.TicketEntregaDao;
import com.example.myapplication.models.Estudiante;
import com.example.myapplication.models.ProgramaProfesional;
import com.example.myapplication.models.TicketEntrega;

@Database(entities = {Estudiante.class, ProgramaProfesional.class, TicketEntrega.class}, version = 3, exportSchema = false)
public abstract class EstudianteDatabase extends RoomDatabase {
    private static volatile EstudianteDatabase instancia;

    public abstract EstudianteDao estudianteDao();
    public abstract ProgramaProfesionalDao programaProfesionalDao();
    public abstract TicketEntregaDao ticketEntregaDao();

    public static EstudianteDatabase getInstancia(Context context) {
        if (instancia == null) {
            synchronized (EstudianteDatabase.class) {
                if (instancia == null) {
                    instancia = Room.databaseBuilder(context.getApplicationContext(),
                                    EstudianteDatabase.class, "estudiante_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instancia;
    }
}

