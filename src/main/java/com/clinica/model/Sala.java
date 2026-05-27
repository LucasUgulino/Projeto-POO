package com.clinica.model;

public class Sala {

    private int numeroSala;
    private String nome;
    private String tipo;

    public Sala(int numeroSala, String nome, String tipo) {
        this.numeroSala = numeroSala;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public void setNumeroSala(int numeroSala) {
        this.numeroSala = numeroSala;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
