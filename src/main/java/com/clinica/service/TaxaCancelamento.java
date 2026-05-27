package com.clinica.service;

import com.clinica.model.Agendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Calcula taxa de cancelamento com base na antecedência do cancelamento.
 *
 * Regra padrão: sem taxa se cancelado com 24h ou mais de antecedência;
 * taxa de 20% do valor base do profissional caso contrário.
 */
public class TaxaCancelamento {

    private static final long   HORAS_PRAZO      = 24;
    private static final double PERCENTUAL_TAXA   = 0.20;

    /**
     * Calcula a taxa de cancelamento.
     *
     * @param agendamento       agendamento que está sendo cancelado
     * @param dataCancelamento  data em que o cancelamento ocorre
     * @param horaCancelamento  hora em que o cancelamento ocorre
     * @return valor da taxa (0.0 se dentro do prazo)
     */
    public double calcularTaxa(Agendamento agendamento,
                               LocalDate dataCancelamento,
                               LocalTime horaCancelamento) {
        LocalDateTime momentoConsulta     = LocalDateTime.of(agendamento.getData(), agendamento.getHora());
        LocalDateTime momentoCancelamento = LocalDateTime.of(dataCancelamento, horaCancelamento);

        long horasRestantes = ChronoUnit.HOURS.between(momentoCancelamento, momentoConsulta);

        if (horasRestantes >= HORAS_PRAZO) {
            return 0.0;
        }
        return agendamento.getProfissional().getValorConsulta() * PERCENTUAL_TAXA;
    }
}
