package com.clinica.service;

import com.clinica.model.Agendamento;
import com.clinica.model.Consulta;
import com.clinica.model.Procedimento;
import com.clinica.model.Recibo;

public class FaturamentoService {

    private double percentualDescontoConvenio;

    public FaturamentoService() {
        percentualDescontoConvenio = 30.0;
    }

    public double calcularValor(Agendamento agendamento) {
        double valor = agendamento.calcularValor();

        if (agendamento instanceof Consulta) {
            Consulta consulta = (Consulta) agendamento;

            if (consulta.getTipoConsulta().equalsIgnoreCase("RETORNO")) {
                valor = consulta.getProfissional().getValorConsulta() * 0.5;
            }
        }

        if (agendamento instanceof Procedimento) {
            Procedimento procedimento = (Procedimento) agendamento;
            valor = procedimento.getValor();
        }

        if (temConvenio(agendamento)) {
            valor = valor - (valor * percentualDescontoConvenio / 100);
        }

        return valor;
    }

    public Recibo gerarRecibo(Agendamento agendamento) {
        String servico = "Agendamento";

        if (agendamento instanceof Consulta) {
            servico = "Consulta";
        }

        if (agendamento instanceof Procedimento) {
            servico = "Procedimento";
        }

        return new Recibo(
                agendamento.getPaciente(),
                agendamento.getProfissional(),
                agendamento.getData(),
                servico,
                calcularValor(agendamento));
    }

    public double getPercentualDescontoConvenio() {
        return percentualDescontoConvenio;
    }

    public void setPercentualDescontoConvenio(double percentualDescontoConvenio) {
        this.percentualDescontoConvenio = percentualDescontoConvenio;
    }

    private boolean temConvenio(Agendamento agendamento) {
        String convenio = agendamento.getPaciente().getConvenio();
        return convenio != null && !convenio.equals("");
    }
}
