package com.clinica.model;

import java.util.ArrayList;
import java.util.List;

public class FilaEspera {

    private final List<Paciente> fila;

    public FilaEspera() {
        this.fila = new ArrayList<>();
    }

    public void adicionar(Paciente paciente) {
        fila.add(paciente);
    }

    public Paciente proximo() {
        if (fila.isEmpty()) {
            return null;
        }
        return fila.remove(0);
    }

    public boolean estaVazia() {
        return fila.isEmpty();
    }

    public int tamanho() {
        return fila.size();
    }

    public List<Paciente> getFila() {
        return fila;
    }
}
