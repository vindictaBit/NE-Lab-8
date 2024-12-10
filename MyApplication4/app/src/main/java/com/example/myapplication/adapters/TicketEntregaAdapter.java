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

    public TicketEntregaAdapter(List<TicketEntrega> tickets) {
        this.tickets = tickets;
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
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumeroTicket, tvDescripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumeroTicket = itemView.findViewById(R.id.tv_numero_ticket);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_ticket);
        }
    }
}