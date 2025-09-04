package com.example.telequiz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pregunta {
    private String texto;
    private String[] opciones;
    private int respuestaCorrecta;

    public Pregunta(String texto, String[] opciones, int respuestaCorrecta) {
        this.texto = texto;
        this.opciones = opciones.clone();
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public void mezclarOpciones() {
        String correcta = opciones[respuestaCorrecta];
        List<String> lista = Arrays.asList(opciones);
        Collections.shuffle(lista);
        opciones = lista.toArray(new String[0]);

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(correcta)) {
                respuestaCorrecta = i;
                break;
            }
        }
    }

    public String getTexto() { return texto; }
    public String[] getOpciones() { return opciones; }
    public int getRespuestaCorrecta() { return respuestaCorrecta; }
}
