package com.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;
import com.clinica.util.StatusAgendamento;

public class Consulta extends Agendamento {

    private String tipoConsulta;

    public Consulta(Paciente paciente, Profissional profissional, Sala sala, LocalDate data, LocalTime hora,
            StatusAgendamento status, String tipoConsulta) {
        super(paciente, profissional, sala, data, hora, status);
        this.tipoConsulta = tipoConsulta;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }

    @Override
    public double calcularValor() {

        if (tipoConsulta.equals("RETORNO")) {
            return 0.0;
        }
        return getProfissional().getValorConsulta();
    }
}
