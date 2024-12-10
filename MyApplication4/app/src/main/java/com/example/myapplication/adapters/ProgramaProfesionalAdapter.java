package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.models.ProgramaProfesional;
import java.util.List;

public class ProgramaProfesionalAdapter extends RecyclerView.Adapter<ProgramaProfesionalAdapter.ViewHolder> {
    private List<ProgramaProfesional> programas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProgramaProfesional programa);
    }

    public ProgramaProfesionalAdapter(List<ProgramaProfesional> programas, OnItemClickListener listener) {
        this.programas = programas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_programa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProgramaProfesional programa = programas.get(position);
        holder.tvNombre.setText(programa.getNombre());
        holder.tvEstado.setText(programa.getEstado());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(programa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_programa_nombre);
            tvEstado = itemView.findViewById(R.id.tv_programa_estado);
        }
    }
}

