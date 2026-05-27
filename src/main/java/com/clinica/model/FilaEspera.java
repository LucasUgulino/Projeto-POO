package com.clinica.model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Fila de espera de pacientes aguardando horário disponível.
 * Usa composição com Queue para garantir a ordem FIFO.
 */
public class FilaEspera {

    private final Queue<Paciente> fila;

    public FilaEspera() {
        this.fila = new LinkedList<>();
    }

    /** Adiciona um paciente ao final da fila. */
    public void adicionar(Paciente paciente) {
        fila.add(paciente);
    }

    /**
     * Remove e retorna o próximo paciente da fila.
     *
     * @return próximo paciente, ou {@code null} se a fila estiver vazia
     */
    public Paciente proximo() {
        return fila.poll();
    }

    /** Verifica se a fila está vazia. */
    public boolean estaVazia() {
        return fila.isEmpty();
    }

    /** Retorna o número de pacientes na fila sem removê-los. */
    public int tamanho() {
        return fila.size();
    }

    public Queue<Paciente> getFila() {
        return fila;
    }
}
