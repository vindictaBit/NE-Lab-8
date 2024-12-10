package com.example.myapplication.dao;

import androidx.room.*;
import com.example.myapplication.models.ProgramaProfesional;
import java.util.List;

@Dao
public interface ProgramaProfesionalDao {
    @Insert
    void insertar(ProgramaProfesional programa);

    @Update
    void actualizar(ProgramaProfesional programa);

    @Delete
    void eliminar(ProgramaProfesional programa);

    @Query("SELECT * FROM programa_profesional ORDER BY nombre ASC")
    List<ProgramaProfesional> obtenerTodos();

    @Query("SELECT * FROM programa_profesional WHERE estado = 'A' ORDER BY nombre ASC")
    List<ProgramaProfesional> obtenerActivos();
}
