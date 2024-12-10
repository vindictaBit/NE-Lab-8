package com.example.myapplication.dao;

import androidx.room.*;

import com.example.myapplication.models.ProgramaProfesional;
import com.example.myapplication.models.TicketEntrega;

import java.util.List;

@Dao
public interface TicketEntregaDao {
    @Insert
    void insertar(TicketEntrega ticket);

    @Update
    void actualizar(TicketEntrega ticket);

    @Delete
    void eliminar(TicketEntrega ticket);

    @Query("SELECT * FROM ticket_entrega WHERE estado = 'A' ORDER BY fecha ASC")
    List<TicketEntrega> obtenerActivos();

    @Query("SELECT * FROM ticket_entrega WHERE numeroTicket = :numeroTicket")
    TicketEntrega buscarPorNumero(String numeroTicket);

    @Query("SELECT * FROM ticket_entrega ORDER BY numeroTicket ASC")
    List<TicketEntrega> obtenerTodos();
}
