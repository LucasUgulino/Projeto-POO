package com.clinica.model;

import java.util.LinkedList;
import java.util.Queue;


public class FilaEspera {

    private final Queue<Paciente> fila;

    public FilaEspera() {
        this.fila = new LinkedList<>();
    }

   
    public void adicionar(Paciente paciente) {
        fila.add(paciente);
    }

    
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
