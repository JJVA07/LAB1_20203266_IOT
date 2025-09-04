package com.example.telequiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ResultadosActivity extends AppCompatActivity {

    // Barra superior
    private TextView tvNombreApp;
    private ImageButton btnVolver, btnPerfil;

    // Contenido principal
    private TextView tvTituloResultado, tvPuntajeFinal, tvTiempo, tvMensaje;
    private Button btnVolverJugar, btnVerPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        inicializarVistas();
        configurarListeners();
        mostrarResultados();

        // Forzar que el botón físico "atrás" lleve a Selección de Temática
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                irASeleccionTematica();
            }
        });
    }

    private void inicializarVistas() {
        // Barra superior
        tvNombreApp = findViewById(R.id.tvNombreApp);
        btnVolver = findViewById(R.id.btnVolver);
        btnPerfil = findViewById(R.id.btnPerfil);

        // Contenido principal
        tvTituloResultado = findViewById(R.id.tvTituloResultado);
        tvPuntajeFinal = findViewById(R.id.tvPuntajeFinal);
        tvTiempo = findViewById(R.id.tvTiempo);
        tvMensaje = findViewById(R.id.tvMensaje);

        btnVolverJugar = findViewById(R.id.btnVolverJugar);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
    }

    private void configurarListeners() {
        // Flecha retroceder en la barra superior
        btnVolver.setOnClickListener(v -> irASeleccionTematica());

        // Botón perfil en la barra superior
        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(ResultadosActivity.this, PerfilEstadisticasActivity.class);
            startActivity(intent);
        });

        // Botón inferior: volver a jugar
        btnVolverJugar.setOnClickListener(v -> irASeleccionTematica());

        // Botón inferior: ver perfil
        btnVerPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(ResultadosActivity.this, PerfilEstadisticasActivity.class);
            startActivity(intent);
        });
    }

    private void mostrarResultados() {
        int puntajeTotal = getIntent().getIntExtra("puntajeTotal", 0);
        int duracion = getIntent().getIntExtra("duracion", 0);
        String tematica = getIntent().getStringExtra("tematica");

        // Actualizar barra superior
        tvNombreApp.setText("TeleQuiz - " + tematica);

        // Mostrar resultados
        tvTituloResultado.setText("¡Juego Terminado!");
        tvPuntajeFinal.setText("Puntaje Final: " + puntajeTotal);

        int min = duracion / 60, seg = duracion % 60;
        tvTiempo.setText(String.format("Tiempo: %02d:%02d", min, seg));

        if (puntajeTotal >= 0) {
            tvPuntajeFinal.setTextColor(Color.parseColor("#4CAF50"));
            tvMensaje.setText("¡Felicitaciones! Has obtenido un puntaje positivo en " + tematica);
        } else {
            tvPuntajeFinal.setTextColor(Color.parseColor("#F44336"));
            tvMensaje.setText("¡No te desanimes! Intenta otra vez en " + tematica);
        }
    }

    private void irASeleccionTematica() {
        Intent intent = new Intent(ResultadosActivity.this, SeleccionTematicaActivity.class);
        startActivity(intent);
        finish();
    }
}
