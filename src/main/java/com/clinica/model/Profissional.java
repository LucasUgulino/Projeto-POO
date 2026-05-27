package com.clinica.model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Profissional extends Pessoa {

    private String especialidade;
    private double valorConsulta;
    private ArrayList<LocalTime> horarios;
    private int duracaoAtendimento;

    public Profissional(String nome, String cpf, String contato, String especialidade, double valorConsulta,
            int duracaoAtendimento) {
        super(nome, cpf, contato);
        this.especialidade = especialidade;
        this.valorConsulta = valorConsulta;
        this.horarios = new ArrayList<>();
        this.duracaoAtendimento = duracaoAtendimento;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public double getValorConsulta() {
        return valorConsulta;
    }

    public void setValorConsulta(double valorConsulta) {
        this.valorConsulta = valorConsulta;
    }

    public ArrayList<LocalTime> getHorarios() {
        return horarios;
    }

    public int getDuracaoAtendimento() {
        return duracaoAtendimento;
    }

    public void adicionarHorario(LocalTime horario) {
        horarios.add(horario);
    }

    public void removerHorario(LocalTime horarioAntigo) {
        horarios.remove(horarioAntigo);
    }

}
