package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Estudiante;

import java.util.List;

public class EstudianteAdapter extends RecyclerView.Adapter<EstudianteAdapter.ViewHolder> {
    private List<Estudiante> estudiantes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Estudiante estudiante);
    }

    public EstudianteAdapter(List<Estudiante> estudiantes, OnItemClickListener listener) {
        this.estudiantes = estudiantes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estudiante, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Estudiante estudiante = estudiantes.get(position);
        holder.tvNombre.setText(estudiante.getNombre());
        holder.tvEstado.setText(estudiante.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(estudiante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return estudiantes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvEstado = itemView.findViewById(R.id.tv_estado);
        }
    }
}
