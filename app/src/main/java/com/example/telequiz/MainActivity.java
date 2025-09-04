package com.example.telequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText etNombreJugador;
    private Button btnEntrarJuego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombreJugador = findViewById(R.id.etNombreJugador);
        btnEntrarJuego = findViewById(R.id.btnEntrarJuego);

        btnEntrarJuego.setOnClickListener(v -> {
            String nombreJugador = etNombreJugador.getText().toString().trim();

            if (!nombreJugador.isEmpty()) {
                SharedPreferences prefs = getSharedPreferences("TeleQuizPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nombreJugador", nombreJugador);

                if (!prefs.contains("fechaInicioJuego")) {
                    String fechaInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(new Date());
                    editor.putString("fechaInicioJuego", fechaInicio);
                }

                editor.apply();

                Intent intent = new Intent(MainActivity.this, SeleccionTematicaActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Por favor ingresa tu nombre", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
