package com.clinica.service;

import com.clinica.model.Agendamento;
import com.clinica.model.FilaEspera;
import com.clinica.model.Paciente;
import com.clinica.model.Profissional;
import com.clinica.util.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável por gerenciar agendamentos:
 * criação, conflito de horário e cancelamento com integração à fila de espera.
 */
public class AgendamentoService {

    private final List<Agendamento> agendamentos;
    private final FilaEspera filaEspera;

    public AgendamentoService() {
        this.agendamentos = new ArrayList<>();
        this.filaEspera = new FilaEspera();
    }

    public AgendamentoService(FilaEspera filaEspera) {
        this.agendamentos = new ArrayList<>();
        this.filaEspera = filaEspera;
    }

    /**
     * Adiciona um agendamento verificando conflito de horário antes.
     *
     * @throws IllegalStateException se já houver agendamento ativo para o mesmo profissional na mesma data/hora
     */
    public void agendar(Agendamento agendamento) {
        if (verificarConflito(agendamento.getProfissional(), agendamento.getData(), agendamento.getHora())) {
            throw new IllegalStateException(
                    "Conflito de horário: profissional já possui agendamento ativo neste horário.");
        }
        agendamentos.add(agendamento);
    }

    /**
     * Verifica se o profissional já possui um agendamento ativo na data e hora informadas.
     *
     * @return {@code true} se houver conflito, {@code false} caso contrário
     */
    public boolean verificarConflito(Profissional profissional, LocalDate data, LocalTime hora) {
        for (Agendamento a : agendamentos) {
            if (a.getStatus() == StatusAgendamento.CANCELADO) {
                continue;
            }
            if (a.getProfissional().getCpf().equals(profissional.getCpf())
                    && a.getData().equals(data)
                    && a.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cancela um agendamento e retorna o próximo paciente da fila de espera,
     * caso haja alguém aguardando para ocupar o horário.
     *
     * @param agendamento agendamento a cancelar
     * @param motivo      motivo do cancelamento
     * @return próximo paciente da fila, ou {@code null} se a fila estiver vazia
     */
    public Paciente cancelar(Agendamento agendamento, String motivo) {
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        if (!filaEspera.estaVazia()) {
            return filaEspera.proximo();
        }
        return null;
    }

    public void finalizar(Agendamento agendamento) {
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public FilaEspera getFilaEspera() {
        return filaEspera;
    }
}
