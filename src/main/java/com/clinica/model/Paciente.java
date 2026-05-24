package com.clinica.model;

import java.util.ArrayList;

public class Paciente extends Pessoa {

    private String convenio;
    private String prioridade;
    private ArrayList<String> historico;

    public Paciente(String nome, String cpf, String contato, String convenio, String prioridade) {
        super(nome, cpf, contato);
        this.convenio = convenio;
        this.prioridade = prioridade;
        this.historico = new ArrayList<>();
    }

    public String getConvenio() {
        return convenio;
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
