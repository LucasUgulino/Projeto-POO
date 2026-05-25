package com.clinica.model;

import java.time.LocalDate;

public class Recibo {

    private Paciente paciente;
    private Profissional profissional;
    private LocalDate data;
    private String servico;
    private double totalCobrado;
    
    public Recibo(Paciente paciente, Profissional profissional, LocalDate data, String servico, double totalCobrado) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.data = data;
        this.servico = servico;
        this.totalCobrado = totalCobrado;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public double getTotalCobrado() {
        return totalCobrado;
    }

    public void setTotalCobrado(double totalCobrado) {
        this.totalCobrado = totalCobrado;
    }

    
}
