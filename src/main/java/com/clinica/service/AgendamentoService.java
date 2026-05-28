package com.clinica.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.clinica.model.Agendamento;
import com.clinica.model.Consulta;
import com.clinica.model.FilaEspera;
import com.clinica.model.Paciente;
import com.clinica.model.Procedimento;
import com.clinica.model.Profissional;
import com.clinica.model.Sala;
import com.clinica.util.StatusAgendamento;

public class AgendamentoService {

    private ArrayList<Agendamento> agendamentos;
    private TaxaCancelamento taxaCancelamento;
    private FilaEspera filaEspera;

    public AgendamentoService() {
        agendamentos = new ArrayList<>();
        taxaCancelamento = new TaxaCancelamento();
        filaEspera = new FilaEspera();
    }

    public AgendamentoService(FilaEspera filaEspera) {
        agendamentos = new ArrayList<>();
        taxaCancelamento = new TaxaCancelamento();
        this.filaEspera = filaEspera;
    }

    public boolean agendar(Consulta consulta) {
        if (consulta == null) {
            return false;
        }

        if (verificarConflito(consulta.getProfissional(), consulta.getSala(), consulta.getData(), consulta.getHora())) {
            return false;
        }

        consulta.setStatus(StatusAgendamento.AGENDADO);
        agendamentos.add(consulta);
        return true;
    }

    public boolean agendar(Procedimento procedimento) {
        if (procedimento == null) {
            return false;
        }

        if (verificarConflito(procedimento.getProfissional(), procedimento.getSala(), procedimento.getData(), procedimento.getHora())) {
            return false;
        }

        procedimento.setStatus(StatusAgendamento.AGENDADO);
        agendamentos.add(procedimento);
        return true;
    }

    public double cancelar(int id, String motivo) {
        Agendamento agendamento = buscarPorId(id);

        if (agendamento == null) {
            return 0.0;
        }

        if (motivo == null || motivo.equals("")) {
            return 0.0;
        }

        double taxa = taxaCancelamento.calcularTaxa(agendamento, LocalDateTime.now());
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        return taxa;
    }

    public Paciente cancelar(Agendamento agendamento, String motivo) {
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        if (!filaEspera.estaVazia()) {
            return filaEspera.proximo();
        }
        return null;
    }

    public boolean finalizar(int id) {
        Agendamento agendamento = buscarPorId(id);

        if (agendamento == null) {
            return false;
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            return false;
        }

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        return true;
    }

    public boolean verificarConflito(Profissional profissional, Sala sala, LocalDate data, LocalTime hora) {
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getStatus() != StatusAgendamento.CANCELADO) {
                boolean mesmaData = agendamento.getData().equals(data);
                boolean mesmoHorario = agendamento.getHora().equals(hora);
                boolean mesmoProfissional = agendamento.getProfissional().equals(profissional);
                boolean mesmaSala = agendamento.getSala().equals(sala);

                if (mesmaData && mesmoHorario && (mesmoProfissional || mesmaSala)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Agendamento buscarPorId(int id) {
        if (id < 0 || id >= agendamentos.size()) {
            return null;
        }

        return agendamentos.get(id);
    }

    public ArrayList<Agendamento> listarTodos() {
        return agendamentos;
    }
}
