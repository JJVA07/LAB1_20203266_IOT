package com.example.telequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SeleccionTematicaActivity extends AppCompatActivity {
    private TextView tvNombreApp, tvNombreJugador;
    private ImageButton btnVolver, btnPerfil;   // 👈 Ahora son ImageButton
    private Button btnRedes, btnCiberseguridad, btnMicroondas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_tematica);

        inicializarVistas();
        configurarListeners();
        cargarDatosJugador();
    }

    private void inicializarVistas() {
        tvNombreApp = findViewById(R.id.tvNombreApp);
        tvNombreJugador = findViewById(R.id.tvNombreJugador);

        btnVolver = findViewById(R.id.btnVolver);       // ImageButton
        btnPerfil = findViewById(R.id.btnPerfil);       // ImageButton

        btnRedes = findViewById(R.id.btnRedes);
        btnCiberseguridad = findViewById(R.id.btnCiberseguridad);
        btnMicroondas = findViewById(R.id.btnMicroondas);
    }

    private void configurarListeners() {
        // Cierra la actividad actual
        btnVolver.setOnClickListener(v -> finish());

        // Abre la pantalla de perfil
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilEstadisticasActivity.class);
            startActivity(intent);
        });

        // Inicia el juego con la temática seleccionada
        btnRedes.setOnClickListener(v -> iniciarJuego("Redes"));
        btnCiberseguridad.setOnClickListener(v -> iniciarJuego("Ciberseguridad"));
        btnMicroondas.setOnClickListener(v -> iniciarJuego("Microondas"));
    }

    private void cargarDatosJugador() {
        SharedPreferences prefs = getSharedPreferences("TeleQuizPrefs", MODE_PRIVATE);
        String nombreJugador = prefs.getString("nombreJugador", "Jugador");
        tvNombreJugador.setText("Bienvenido, " + nombreJugador);
    }

    private void iniciarJuego(String tematica) {
        Intent intent = new Intent(this, JuegoActivity.class);
        intent.putExtra("tematica", tematica);
        startActivity(intent);
    }
}
