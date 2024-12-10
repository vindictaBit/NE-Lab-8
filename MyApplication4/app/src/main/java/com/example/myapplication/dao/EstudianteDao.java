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

    // Método para obtener solo los estudiantes activos
    @Query("SELECT * FROM estudiantes WHERE estado = 'A' ORDER BY nombre ASC")
    List<Estudiante> obtenerActivos();

    @Query("UPDATE estudiantes SET estado = '*' WHERE id = :id")
    void eliminarLogico(int id);

    @Query("UPDATE estudiantes SET estado = 'I' WHERE id = :id")
    void inactivar(int id);

    @Query("UPDATE estudiantes SET estado = 'A' WHERE id = :id")
    void reactivar(int id);

    @Query("SELECT * FROM estudiantes WHERE nombre LIKE :filtro AND estado = 'A'")
    List<Estudiante> buscarPorNombre(String filtro);

    @Query("SELECT * FROM estudiantes ORDER BY nombre ASC")
    List<Estudiante> ordenarPorNombre();

    @Query("SELECT * FROM estudiantes ORDER BY id ASC")
    List<Estudiante> ordenarPorCodigo();
}
