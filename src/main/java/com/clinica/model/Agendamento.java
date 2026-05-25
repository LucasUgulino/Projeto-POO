package com.clinica.model;

import java.time.LocalDate;
import java.time.LocalTime;
import com.clinica.util.StatusAgendamento;

public abstract class Agendamento implements Cobravel {

    private Paciente paciente;
    private Profissional profissional;
    private Sala sala;
    private LocalDate data;
    private LocalTime hora;
    private StatusAgendamento status;

    public Agendamento(Paciente paciente, Profissional profissional, Sala sala, LocalDate data, LocalTime hora,
            StatusAgendamento status) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.sala = sala;
        this.data = data;
        this.hora = hora;
        this.status = status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

}
