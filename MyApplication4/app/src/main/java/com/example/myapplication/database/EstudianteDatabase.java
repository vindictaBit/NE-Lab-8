package com.example.myapplication.database;

import android.content.Context;

import androidx.room.*;

import com.example.myapplication.dao.EstudianteDao;
import com.example.myapplication.models.Estudiante;

@Database(entities = {Estudiante.class}, version = 1, exportSchema = false)
public abstract class EstudianteDatabase extends RoomDatabase {
    private static volatile EstudianteDatabase instancia;

    public abstract EstudianteDao estudianteDao();

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
