package com.example.myapplication.helpers;

import android.content.Context;
import android.view.View;
import androidx.appcompat.app.AlertDialog;

public class DialogHelper {

    public interface ConfirmacionCallback {
        void onConfirmar();
    }

    public static void mostrarConfirmacion(Context context, String titulo, String mensaje, ConfirmacionCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Sí", (dialog, which) -> callback.onConfirmar())
                .setNegativeButton("No", null)
                .show();
    }

    public static void mostrarFormularioEdicion(Context context, String titulo, View formulario, ConfirmacionCallback callback) {
        new AlertDialog.Builder(context)
                .setTitle(titulo)
                .setView(formulario)
                .setPositiveButton("Guardar", (dialog, which) -> callback.onConfirmar())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
