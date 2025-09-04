package com.example.telequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.*;

public class PerfilEstadisticasActivity extends AppCompatActivity {
    private TextView tvNombreJugador, tvFechaInicio, tvPartidasJugadas, tvTituloHistorial, tvNombreApp;
    private LinearLayout layoutHistorial;
    private ImageButton btnVolver, btnPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_estadisticas);

        inicializarVistas();
        cargarDatosUsuario();
        cargarHistorialPartidas();
        configurarListeners();
    }

    private void inicializarVistas() {
        tvNombreJugador = findViewById(R.id.tvNombreJugador);
        tvFechaInicio = findViewById(R.id.tvFechaInicio);
        tvPartidasJugadas = findViewById(R.id.tvPartidasJugadas);
        tvTituloHistorial = findViewById(R.id.tvTituloHistorial);
        layoutHistorial = findViewById(R.id.layoutHistorial);

        // Barra superior
        tvNombreApp = findViewById(R.id.tvNombreApp);
        btnVolver = findViewById(R.id.btnVolver);
        btnPerfil = findViewById(R.id.btnPerfil);
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences("TeleQuizPrefs", MODE_PRIVATE);

        String nombreJugador = prefs.getString("nombreJugador", "Jugador");
        String fechaInicio = prefs.getString("fechaInicioJuego", "No registrada");
        int partidasJugadas = prefs.getInt("partidasJugadas", 0);

        tvNombreJugador.setText("Jugador: " + nombreJugador);
        tvFechaInicio.setText("Fecha de inicio: " + fechaInicio);
        tvPartidasJugadas.setText("Partidas jugadas: " + partidasJugadas);

        // Título de la barra superior
        tvNombreApp.setText("TeleQuiz - Perfil");
    }

    private void cargarHistorialPartidas() {
        SharedPreferences prefs = getSharedPreferences("TeleQuizPrefs", MODE_PRIVATE);
        Set<String> historialSet = prefs.getStringSet("historialPartidas", new HashSet<>());

        boolean partidaEnCurso = getIntent().getBooleanExtra("partidaEnCurso", false);
        List<EntradaHistorial> historial = new ArrayList<>();

        for (String entrada : historialSet) {
            String[] partes = entrada.split("\\|");
            if (partes.length == 4) {
                historial.add(new EntradaHistorial(partes[0], partes[1], partes[2], partes[3]));
            }
        }

        if (partidaEnCurso) {
            String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            historial.add(new EntradaHistorial(fechaActual, "En curso", "En curso", "0"));
        }

        Collections.sort(historial, (a, b) -> b.fecha.compareTo(a.fecha));
        layoutHistorial.removeAllViews();

        if (historial.isEmpty()) {
            TextView tvSinHistorial = new TextView(this);
            tvSinHistorial.setText("No hay partidas registradas");
            tvSinHistorial.setTextSize(16);
            tvSinHistorial.setPadding(16, 16, 16, 16);
            layoutHistorial.addView(tvSinHistorial);
        } else {
            for (EntradaHistorial entrada : historial) {
                View vistaEntrada = crearVistaEntrada(entrada);
                layoutHistorial.addView(vistaEntrada);
            }
        }
    }

    private View crearVistaEntrada(EntradaHistorial entrada) {
        LinearLayout layoutEntrada = new LinearLayout(this);
        layoutEntrada.setOrientation(LinearLayout.VERTICAL);
        layoutEntrada.setPadding(16, 16, 16, 16);
        layoutEntrada.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);

        TextView tvFechaTematica = new TextView(this);
        tvFechaTematica.setText(entrada.fecha + " - " + entrada.tematica);
        tvFechaTematica.setTextSize(14);
        tvFechaTematica.setTextColor(Color.BLACK);
        layoutEntrada.addView(tvFechaTematica);

        TextView tvResultado = new TextView(this);
        if (entrada.resultado.equals("En curso")) {
            tvResultado.setText("Partida en curso");
            tvResultado.setTextColor(Color.parseColor("#FF9800"));
        } else if (entrada.resultado.equals("Canceló")) {
            tvResultado.setText("Partida cancelada");
            tvResultado.setTextColor(Color.parseColor("#757575"));
        } else {
            int puntaje = Integer.parseInt(entrada.resultado);
            tvResultado.setText("Puntaje: " + puntaje);
            if (puntaje >= 0) tvResultado.setTextColor(Color.parseColor("#4CAF50"));
            else tvResultado.setTextColor(Color.parseColor("#F44336"));
        }
        tvResultado.setTextSize(16);
        tvResultado.setTypeface(null, android.graphics.Typeface.BOLD);
        layoutEntrada.addView(tvResultado);

        if (!entrada.resultado.equals("Canceló") && !entrada.resultado.equals("En curso")) {
            TextView tvTiempo = new TextView(this);
            int duracion = Integer.parseInt(entrada.duracion);
            int minutos = duracion / 60;
            int segundos = duracion % 60;
            tvTiempo.setText(String.format("Tiempo: %02d:%02d", minutos, segundos));
            tvTiempo.setTextSize(12);
            tvTiempo.setTextColor(Color.parseColor("#666666"));
            layoutEntrada.addView(tvTiempo);
        }

        return layoutEntrada;
    }

    private void configurarListeners() {
        // Flecha volver
        btnVolver.setOnClickListener(v -> finish());

        // Perfil (puedes redirigir a otra activity si quieres)
        btnPerfil.setOnClickListener(v -> {
            // Por ahora simplemente mostramos que funciona
            // Si quieres abrir otra pantalla, cambia aquí
        });
    }

    private static class EntradaHistorial {
        String fecha, tematica, resultado, duracion;
        EntradaHistorial(String fecha, String tematica, String resultado, String duracion) {
            this.fecha = fecha;
            this.tematica = tematica;
            this.resultado = resultado;
            this.duracion = duracion;
        }
    }
}
