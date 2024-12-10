package com.example.myapplication.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "ticket_entrega",
        foreignKeys = {
                @ForeignKey(entity = Estudiante.class,
                        parentColumns = "id",
                        childColumns = "estudiante_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = ProgramaProfesional.class,
                        parentColumns = "id",
                        childColumns = "programa_id",
                        onDelete = ForeignKey.CASCADE)
        })
public class TicketEntrega {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String numeroTicket;
    private String fecha;
    private String descripcion;
    private String estado;

    @ColumnInfo(name = "estudiante_id")
    private int estudianteId;

    @ColumnInfo(name = "programa_id")
    private int programaId;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumeroTicket() { return numeroTicket; }
    public void setNumeroTicket(String numeroTicket) { this.numeroTicket = numeroTicket; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }
    public int getProgramaId() { return programaId; }
    public void setProgramaId(int programaId) { this.programaId = programaId; }
}
