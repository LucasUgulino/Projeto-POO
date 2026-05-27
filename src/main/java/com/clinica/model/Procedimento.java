package com.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;
import com.clinica.util.StatusAgendamento;


public class Procedimento extends Agendamento {

    private String descricao;
    private double valor;

    public Procedimento(Paciente paciente, Profissional profissional, Sala sala, LocalDate data, LocalTime hora,
            StatusAgendamento status, String descricao, double valor) {
        super(paciente, profissional, sala, data, hora, status);
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    @Override
    public double calcularValor() {
        return valor;
    }

}
