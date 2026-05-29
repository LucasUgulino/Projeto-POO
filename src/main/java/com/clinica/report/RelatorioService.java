package com.clinica.report;

import com.clinica.model.Agendamento;
import com.clinica.model.Profissional;
import com.clinica.util.StatusAgendamento;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {

    private List<Agendamento> agendamentos;

    public RelatorioService(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
    }

    public List<Agendamento> listarPorProfissional(Profissional profissional) {
        List<Agendamento> resultado = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getProfissional().equals(profissional)) {
                resultado.add(agendamento);
            }
        }
        return resultado;
    }

    public List<Agendamento> listarPorStatus(StatusAgendamento status) {
        List<Agendamento> resultado = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            if (agendamento.getStatus() == status) {
                resultado.add(agendamento);
            }
        }
        return resultado;
    }


}
