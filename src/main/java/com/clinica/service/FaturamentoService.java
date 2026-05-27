package com.clinica.service;

import com.clinica.model.Agendamento;
import com.clinica.model.Consulta;
import com.clinica.model.Procedimento;

/**
 * Serviço de faturamento.
 *
 * Regras aplicadas:
 * - Particular: valor base do profissional.
 * - Convênio: desconto de 30% sobre o valor base.
 * - Retorno (tipoConsulta == "RETORNO"): 50% do valor base.
 * - Procedimento: valor base do profissional + valorExtra do procedimento.
 */
public class FaturamentoService {

    private static final double DESCONTO_CONVENIO = 0.30;
    private static final double DESCONTO_RETORNO  = 0.50;

    /**
     * Calcula o valor a cobrar para um agendamento conforme as regras de negócio.
     *
     * @param agendamento agendamento a faturar
     * @return valor final em reais
     */
    public double calcularValor(Agendamento agendamento) {
        double valorBase = agendamento.getProfissional().getValorConsulta();
        String convenio  = agendamento.getPaciente().getConvenio();
        boolean temConvenio = convenio != null && !convenio.equalsIgnoreCase("particular");

        if (agendamento instanceof Consulta consulta) {
            if (consulta.getTipoConsulta().equalsIgnoreCase("RETORNO")) {
                return valorBase * DESCONTO_RETORNO;
            }
            return temConvenio ? valorBase * (1 - DESCONTO_CONVENIO) : valorBase;
        }

        if (agendamento instanceof Procedimento procedimento) {
            double total = valorBase + procedimento.getValor();
            return temConvenio ? total * (1 - DESCONTO_CONVENIO) : total;
        }

        return valorBase;
    }
}
