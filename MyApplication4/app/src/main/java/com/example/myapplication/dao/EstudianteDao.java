package com.example.myapplication.dao;

import androidx.room.*;

import com.example.myapplication.models.Estudiante;

import java.util.List;

@Dao
public interface EstudianteDao {
    @Insert
    void insertar(Estudiante estudiante);

    @Update
    void actualizar(Estudiante estudiante);

    @Delete
    void eliminar(Estudiante estudiante);

    @Query("SELECT * FROM estudiantes ORDER BY nombre ASC")
    List<Estudiante> obtenerEstudiantes();
}

