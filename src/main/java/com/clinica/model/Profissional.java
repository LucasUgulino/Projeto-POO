package com.clinica.model;

import java.util.ArrayList;

public class Profissional extends Pessoa {

    private String especialidade;
    private double valorConsulta;
    private ArrayList<String> horários;

    public Profissional(String nome, String cpf, String contato, String especialidade, double valorConsulta) {
        super(nome, cpf, contato);
        this.especialidade = especialidade;
        this.valorConsulta = valorConsulta;
        this.horários = new ArrayList<>();
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

    public ArrayList<String> getHorários() {
        return horários;
    }

    public void adicionarHorario(String horario) {
        horários.add(horario);
    }

    public void removerHorario(String horarioAntigo) {
        horários.remove(horarioAntigo);
        System.out.println("Um Horário Foi Agendado e Removido da Lista de Disponíveis");
    }
}
