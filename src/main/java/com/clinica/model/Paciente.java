package com.clinica.model;

import java.util.ArrayList;

public class Paciente extends Pessoa {

    private int idade;
    private String convenio;
    private String prioridade;
    private ArrayList<String> historico;

    public Paciente(String nome, String cpf, String contato, int idade, String convenio, String prioridade) {
        super(nome, cpf, contato);
        this.idade = idade;
        this.convenio = convenio;
        this.prioridade = prioridade;
        this.historico = new ArrayList<>();
    }

    public String getConvenio() {
        return convenio;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public ArrayList<String> getHistorico() {
        return historico;
    }

    public void adicionarHistorico(String informacoes) {
        historico.add(informacoes);
    }
}
