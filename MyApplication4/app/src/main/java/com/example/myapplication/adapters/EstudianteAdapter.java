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

    public EstudianteAdapter(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
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
    }

    @Override
    public int getItemCount() {
        return estudiantes.size();
    }

    public void actualizarLista(List<Estudiante> nuevosEstudiantes) {
        estudiantes = nuevosEstudiantes;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre);
            tvEstado = itemView.findViewById(R.id.tv_estado);
        }
    }
}
