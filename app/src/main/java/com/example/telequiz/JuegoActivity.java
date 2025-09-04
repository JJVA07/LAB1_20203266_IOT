package com.example.telequiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class JuegoActivity extends AppCompatActivity {
    private TextView tvNombreApp, tvPregunta, tvPuntaje, tvNumeroPregunta;
    private ImageButton btnVolver, btnPerfil;
    private Button btnPistas, btnAnterior, btnSiguiente;
    private Button btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4;

    private String tematica;
    private List<Pregunta> preguntas;
    private int preguntaActual = 0;
    private int puntajeTotal = 0;
    private int pistasUsadas = 0;

    private boolean[] preguntasRespondidas = new boolean[7];
    private boolean[] respuestasCorrectas = new boolean[7];
    private int[] puntajesPorPregunta = new int[7];
    private int[] pistasUsadasPorPregunta = new int[7];

    private int rachaConsecutiva = 0;
    private boolean ultimaRespuestaCorrecta = false;
    private long tiempoInicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        tiempoInicio = System.currentTimeMillis();
        tematica = getIntent().getStringExtra("tematica");

        if (tematica == null) {
            Toast.makeText(this, "Error: no se recibió temática", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        inicializarVistas();
        configurarListeners();
        generarPreguntas();
        mostrarPregunta();
    }

    private void inicializarVistas() {
        tvNombreApp = findViewById(R.id.tvNombreApp);
        tvPregunta = findViewById(R.id.tvPregunta);
        tvPuntaje = findViewById(R.id.tvPuntaje);
        tvNumeroPregunta = findViewById(R.id.tvNumeroPregunta);

        btnVolver = findViewById(R.id.btnVolver);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnPistas = findViewById(R.id.btnPistas);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        btnOpcion1 = findViewById(R.id.btnOpcion1);
        btnOpcion2 = findViewById(R.id.btnOpcion2);
        btnOpcion3 = findViewById(R.id.btnOpcion3);
        btnOpcion4 = findViewById(R.id.btnOpcion4);

        tvNombreApp.setText("TeleQuiz - " + tematica);
        actualizarPuntaje();
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> {
            guardarPartidaCancelada();
            finish();
        });

        btnPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(JuegoActivity.this, PerfilEstadisticasActivity.class);
            intent.putExtra("partidaEnCurso", true);
            startActivity(intent);
        });

        btnPistas.setOnClickListener(v -> usarPista());

        btnAnterior.setOnClickListener(v -> {
            if (preguntaActual > 0) {
                preguntaActual--;
                mostrarPregunta();
            }
        });

        btnSiguiente.setOnClickListener(v -> {
            if (preguntaActual < preguntas.size() - 1) {
                preguntaActual++;
                mostrarPregunta();
            } else if (todasPreguntasRespondidas()) {
                terminarJuego();
            }
        });

        btnOpcion1.setOnClickListener(v -> responderPregunta(btnOpcion1, 0));
        btnOpcion2.setOnClickListener(v -> responderPregunta(btnOpcion2, 1));
        btnOpcion3.setOnClickListener(v -> responderPregunta(btnOpcion3, 2));
        btnOpcion4.setOnClickListener(v -> responderPregunta(btnOpcion4, 3));
    }

    // ===== Generación de preguntas =====
    private void generarPreguntas() {
        preguntas = new ArrayList<>();

        switch (tematica) {
            case "Redes":
                generarPreguntasRedes();
                break;
            case "Ciberseguridad":
                generarPreguntasCiberseguridad();
                break;
            case "Microondas":
                generarPreguntasMicroondas();
                break;
        }

        Collections.shuffle(preguntas);
        if (preguntas.size() > 7) {
            preguntas = preguntas.subList(0, 7);
        }

        for (Pregunta p : preguntas) p.mezclarOpciones();
    }

    private void generarPreguntasRedes() {
        preguntas.add(new Pregunta("¿Qué significa TCP?",
                new String[]{"Transmission Control Protocol", "Transfer Control Protocol", "Transport Control Packet", "Transmission Connection Protocol"}, 0));
        preguntas.add(new Pregunta("¿Cuál es el puerto por defecto de HTTP?",
                new String[]{"80", "443", "21", "25"}, 0));
        preguntas.add(new Pregunta("¿Qué significa IP?",
                new String[]{"Internet Protocol", "Internal Protocol", "Internet Package", "Internal Package"}, 0));
        preguntas.add(new Pregunta("¿Cuál es la dirección IP de loopback?",
                new String[]{"127.0.0.1", "192.168.1.1", "10.0.0.1", "172.16.0.1"}, 0));
        preguntas.add(new Pregunta("¿Qué capa del modelo OSI maneja el enrutamiento?",
                new String[]{"Capa 3 - Red", "Capa 2 - Enlace", "Capa 4 - Transporte", "Capa 1 - Física"}, 0));
        preguntas.add(new Pregunta("¿Qué protocolo se usa para transferir archivos?",
                new String[]{"FTP", "HTTP", "SMTP", "DNS"}, 0));
        preguntas.add(new Pregunta("¿Cuántos bits tiene una dirección IPv4?",
                new String[]{"32", "64", "128", "16"}, 0));
    }

    private void generarPreguntasCiberseguridad() {
        preguntas.add(new Pregunta("¿Qué es un firewall?",
                new String[]{"Sistema de seguridad de red", "Virus informático", "Programa antivirus", "Servidor web"}, 0));
        preguntas.add(new Pregunta("¿Qué significa DDoS?",
                new String[]{"Distributed Denial of Service", "Direct Denial of Service", "Dynamic Denial of Service", "Distributed Data of Service"}, 0));
        preguntas.add(new Pregunta("¿Qué es phishing?",
                new String[]{"Técnica de ingeniería social", "Tipo de malware", "Protocolo de seguridad", "Sistema operativo"}, 0));
        preguntas.add(new Pregunta("¿Qué puerto usa HTTPS?",
                new String[]{"443", "80", "21", "25"}, 0));
        preguntas.add(new Pregunta("¿Qué es un rootkit?",
                new String[]{"Malware que oculta su presencia", "Sistema de autenticación", "Protocolo de encriptación", "Herramienta de backup"}, 0));
        preguntas.add(new Pregunta("¿Qué significa VPN?",
                new String[]{"Virtual Private Network", "Virtual Public Network", "Variable Private Network", "Virtual Protocol Network"}, 0));
        preguntas.add(new Pregunta("¿Qué es un exploit?",
                new String[]{"Código que aprovecha vulnerabilidades", "Antivirus", "Sistema de backup", "Protocolo seguro"}, 0));
    }

    private void generarPreguntasMicroondas() {
        preguntas.add(new Pregunta("¿Qué frecuencia utilizan los hornos microondas?",
                new String[]{"2.45 GHz", "5 GHz", "1 GHz", "10 GHz"}, 0));
        preguntas.add(new Pregunta("¿Quién inventó el horno microondas?",
                new String[]{"Percy Spencer", "James Watt", "Thomas Edison", "Nikola Tesla"}, 0));
        preguntas.add(new Pregunta("¿En qué año se inventó el microondas?",
                new String[]{"1945", "1950", "1960", "1935"}, 0));
        preguntas.add(new Pregunta("¿Qué material NO debe usarse en microondas?",
                new String[]{"Metal", "Vidrio", "Cerámica", "Plástico apto"}, 0));
        preguntas.add(new Pregunta("¿Cómo calientan los alimentos las microondas?",
                new String[]{"Agitando las moléculas de agua", "Por radiación infrarroja", "Por conducción térmica", "Por convección"}, 0));
        preguntas.add(new Pregunta("¿Por qué gira el plato en el microondas?",
                new String[]{"Para calentar uniformemente", "Por decoración", "Para hacer ruido", "Para ahorrar energía"}, 0));
        preguntas.add(new Pregunta("¿Qué es el magnetrón en un microondas?",
                new String[]{"Generador de microondas", "Plato giratorio", "Sistema de ventilación", "Temporizador"}, 0));
    }

    // ===== Mostrar pregunta =====
    private void mostrarPregunta() {
        Pregunta pregunta = preguntas.get(preguntaActual);

        tvNumeroPregunta.setText("Pregunta " + (preguntaActual + 1) + " de " + preguntas.size());
        tvPregunta.setText(pregunta.getTexto());

        String[] opciones = pregunta.getOpciones();
        btnOpcion1.setText("A) " + opciones[0]);
        btnOpcion2.setText("B) " + opciones[1]);
        btnOpcion3.setText("C) " + opciones[2]);
        btnOpcion4.setText("D) " + opciones[3]);

        resetearColoresBotones();

        if (preguntasRespondidas[preguntaActual]) {
            mostrarResultadoPregunta();
        }

        btnAnterior.setEnabled(preguntaActual > 0);
        btnSiguiente.setEnabled(preguntaActual < preguntas.size() - 1);

        btnPistas.setEnabled(pistasUsadas < 3 && pistasUsadasPorPregunta[preguntaActual] == 0 && !preguntasRespondidas[preguntaActual]);
        btnPistas.setText("❓ Pistas (" + (3 - pistasUsadas) + ")");

        actualizarPuntaje();
    }

    private void resetearColoresBotones() {
        int gris = Color.parseColor("#E0E0E0");
        btnOpcion1.setBackgroundColor(gris);
        btnOpcion2.setBackgroundColor(gris);
        btnOpcion3.setBackgroundColor(gris);
        btnOpcion4.setBackgroundColor(gris);
        btnOpcion1.setEnabled(true);
        btnOpcion2.setEnabled(true);
        btnOpcion3.setEnabled(true);
        btnOpcion4.setEnabled(true);
    }

    private void mostrarResultadoPregunta() {
        Pregunta pregunta = preguntas.get(preguntaActual);
        int respuestaCorrecta = pregunta.getRespuestaCorrecta();

        Button[] botones = {btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4};
        botones[respuestaCorrecta].setBackgroundColor(Color.parseColor("#4CAF50"));

        if (!respuestasCorrectas[preguntaActual]) {
            // aquí podrías marcar en rojo la opción seleccionada
        }

        tvPregunta.append("\n\nPuntaje obtenido: " + puntajesPorPregunta[preguntaActual]);
    }

    // ===== Responder =====
    private void responderPregunta(Button botonPresionado, int indiceSeleccionado) {
        if (preguntasRespondidas[preguntaActual]) return;

        Pregunta pregunta = preguntas.get(preguntaActual);
        boolean esCorrecta = indiceSeleccionado == pregunta.getRespuestaCorrecta();
        int puntajePregunta = calcularPuntajePregunta(esCorrecta);

        preguntasRespondidas[preguntaActual] = true;
        respuestasCorrectas[preguntaActual] = esCorrecta;
        puntajesPorPregunta[preguntaActual] = puntajePregunta;
        puntajeTotal += puntajePregunta;

        Button[] botones = {btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4};
        botones[pregunta.getRespuestaCorrecta()].setBackgroundColor(Color.parseColor("#4CAF50"));
        if (!esCorrecta) botonPresionado.setBackgroundColor(Color.parseColor("#F44336"));

        // ✅ ahora se termina desde cualquier pregunta si ya todas fueron respondidas
        if (todasPreguntasRespondidas()) {
            terminarJuego();
        }


        actualizarPuntaje();
    }

    private int calcularPuntajePregunta(boolean esCorrecta) {
        int puntajeBase = esCorrecta ? 2 : -3;
        int multiplicador = 1;

        if (esCorrecta && ultimaRespuestaCorrecta) {
            rachaConsecutiva++;
            multiplicador = (int) Math.pow(2, rachaConsecutiva);
        } else {
            rachaConsecutiva = 0;
        }

        ultimaRespuestaCorrecta = esCorrecta;
        return puntajeBase * multiplicador;
    }

    // ===== Pistas =====
    private void usarPista() {
        if (pistasUsadas >= 3 || pistasUsadasPorPregunta[preguntaActual] > 0 || preguntasRespondidas[preguntaActual]) {
            return;
        }

        Pregunta pregunta = preguntas.get(preguntaActual);
        Button[] botones = {btnOpcion1, btnOpcion2, btnOpcion3, btnOpcion4};

        List<Integer> opcionesIncorrectas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != pregunta.getRespuestaCorrecta()) opcionesIncorrectas.add(i);
        }

        Collections.shuffle(opcionesIncorrectas);
        int opcionADescartar = opcionesIncorrectas.get(0);

        botones[opcionADescartar].setBackgroundColor(Color.parseColor("#BDBDBD"));
        botones[opcionADescartar].setEnabled(false);

        pistasUsadas++;
        pistasUsadasPorPregunta[preguntaActual] = 1;

        btnPistas.setText("❓ Pistas (" + (3 - pistasUsadas) + ")");
        btnPistas.setEnabled(false);

        Toast.makeText(this, "Se eliminó una opción incorrecta", Toast.LENGTH_SHORT).show();
    }

    private boolean todasPreguntasRespondidas() {
        for (boolean r : preguntasRespondidas) if (!r) return false;
        return true;
    }

    // ===== Fin del juego =====
    private void terminarJuego() {
        long tiempoFin = System.currentTimeMillis();
        int duracionSegundos = (int) ((tiempoFin - tiempoInicio) / 1000);

        guardarResultadoPartida(duracionSegundos, false);

        Intent intent = new Intent(JuegoActivity.this, ResultadosActivity.class);
        intent.putExtra("puntajeTotal", puntajeTotal);
        intent.putExtra("duracion", duracionSegundos);
        intent.putExtra("tematica", tematica);
        startActivity(intent);
        finish();
    }

    private void guardarPartidaCancelada() {
        long tiempoFin = System.currentTimeMillis();
        int duracionSegundos = (int) ((tiempoFin - tiempoInicio) / 1000);
        guardarResultadoPartida(duracionSegundos, true);
    }

    private void guardarResultadoPartida(int duracion, boolean cancelada) {
        SharedPreferences prefs = getSharedPreferences("TeleQuizPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int partidasJugadas = prefs.getInt("partidasJugadas", 0) + 1;
        editor.putInt("partidasJugadas", partidasJugadas);

        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String resultado = cancelada ? "Canceló" : String.valueOf(puntajeTotal);
        String entrada = fecha + "|" + tematica + "|" + resultado + "|" + duracion;

        Set<String> historial = prefs.getStringSet("historialPartidas", new HashSet<>());
        historial = new HashSet<>(historial);
        historial.add(entrada);
        editor.putStringSet("historialPartidas", historial);

        editor.apply();
    }

    private void actualizarPuntaje() {
        tvPuntaje.setText("Puntaje: " + puntajeTotal);
        tvPuntaje.setTextColor(puntajeTotal >= 0 ? Color.parseColor("#4CAF50") : Color.parseColor("#F44336"));
    }
}
