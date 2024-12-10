// TicketEntregaAdapter.java
package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.TicketEntrega;

import java.util.List;

public class TicketEntregaAdapter extends RecyclerView.Adapter<TicketEntregaAdapter.ViewHolder> {
    private List<TicketEntrega> tickets;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TicketEntrega ticket);
    }

    public TicketEntregaAdapter(List<TicketEntrega> tickets, OnItemClickListener listener) {
        this.tickets = tickets;
        this.listener = listener;
    }

    public void setTickets(List<TicketEntrega> tickets) {
        this.tickets = tickets;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketEntrega ticket = tickets.get(position);
        holder.tvNumeroTicket.setText("Ticket: " + ticket.getNumeroTicket());
        holder.tvDescripcion.setText("Descripción: " + ticket.getDescripcion());
        holder.tvFecha.setText("Fecha: " + ticket.getFecha());
        holder.tvEstudiante.setText("Estudiante: " + ticket.getEstudianteId());
        holder.tvPrograma.setText("Programa: " + ticket.getProgramaId());
        holder.tvEstado.setText("Estado: " + ticket.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(ticket);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroTicket, tvDescripcion, tvFecha, tvEstudiante, tvPrograma, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroTicket = itemView.findViewById(R.id.tv_numero_ticket);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_ticket);
            tvFecha = itemView.findViewById(R.id.tv_fecha_ticket);
            tvEstudiante = itemView.findViewById(R.id.tv_estudiante_ticket);
            tvPrograma = itemView.findViewById(R.id.tv_programa_ticket);
            tvEstado = itemView.findViewById(R.id.tv_estado);
        }
    }
}