package com.clinica.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.clinica.model.Agendamento;

public class TaxaCancelamento {

    private int horasAntecedencia;
    private double valorTaxa;

    public TaxaCancelamento() {
        horasAntecedencia = 24;
        valorTaxa = 50.0;
    }

    public double calcularTaxa(Agendamento agendamento, LocalDateTime dataHoraCancelamento) {
        LocalDateTime dataHoraAgendamento = LocalDateTime.of(agendamento.getData(), agendamento.getHora());
        long horas = Duration.between(dataHoraCancelamento, dataHoraAgendamento).toHours();

        if (horas >= horasAntecedencia) {
            return 0.0;
        }

        return valorTaxa;
    }

    public int getHorasAntecedencia() {
        return horasAntecedencia;
    }

    public void setHorasAntecedencia(int horasAntecedencia) {
        this.horasAntecedencia = horasAntecedencia;
    }

    public double getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(double valorTaxa) {
        this.valorTaxa = valorTaxa;
    }
}
