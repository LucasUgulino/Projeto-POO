package com.clinica.report;

import com.clinica.model.Agendamento;
import com.clinica.model.Profissional;
import com.clinica.service.FaturamentoService;
import com.clinica.util.StatusAgendamento;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class RelatorioService {

    private List<Agendamento> agendamentos;
    private FaturamentoService faturamentoService;

    public RelatorioService(List<Agendamento> agendamentos) {
        this.agendamentos = agendamentos;
        this.faturamentoService = new FaturamentoService();
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

    public List<Agendamento> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Agendamento> resultado = new ArrayList<>();
        for (Agendamento agendamento : agendamentos) {
            LocalDate data = agendamento.getData();
            if (!data.isBefore(inicio) && !data.isAfter(fim)) {
                resultado.add(agendamento);
            }
        }
        return resultado;
    }

    public void relatorioMensal(int mes, int ano) {
        LocalDate inicio = LocalDate.of(ano, mes, 1);
        LocalDate fim = inicio.withDayOfMonth(inicio.lengthOfMonth());

        List<Agendamento> doMes = listarPorPeriodo(inicio, fim);

        long atendidos = 0;
        long cancelados = 0;
        double receitaTotal = 0;

        for (Agendamento agendamento : doMes) {
            if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
                cancelados++;
            }

            if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
                atendidos++;
                receitaTotal += faturamentoService.calcularValor(agendamento);
            }
        }

        System.out.println("\n========== RELATÓRIO MENSAL ==========");
        System.out.println("Período: " + String.format("%02d/%d", mes, ano));
        System.out.println("Pacientes atendidos: " + atendidos);
        System.out.println("Cancelamentos: " + cancelados);
        System.out.printf("Receita total: R$ %.2f%n", receitaTotal);

        System.out.println("\n--- Receita por Especialidade ---");

        ArrayList<String> especialidades = new ArrayList<>();
        ArrayList<Double> receitas = new ArrayList<>();

        for (Agendamento agendamento : doMes) {
            if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
                String especialidade = agendamento.getProfissional().getEspecialidade();
                int index = especialidades.indexOf(especialidade);
                if (index == -1) {
                    especialidades.add(especialidade);
                    receitas.add(faturamentoService.calcularValor(agendamento));
                } else {
                    receitas.set(index, receitas.get(index) + faturamentoService.calcularValor(agendamento));
                }
            }
        }

        for (int i = 0; i < especialidades.size(); i++) {
            System.out.printf("%-20s R$ %.2f%n", especialidades.get(i), receitas.get(i));
        }

        int totalSlots = doMes.size();
        double taxaOcupacao = 0.0;

        if (totalSlots > 0) {
            taxaOcupacao = (atendidos * 100.0) / totalSlots;
        }

        System.out.println();
        System.out.printf("Taxa de ocupação : %.1f%%\n", taxaOcupacao);
        System.out.println("======================================");
        System.out.println();

        System.out.println("\n--- Profissional Mais Demandado ---");
        String profMaisDemandado = "";
        int maxAtendimentos = 0;

        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<Integer> contagens = new ArrayList<>();

        for (Agendamento agendamento : doMes) {
            if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
                String nome = agendamento.getProfissional().getNome();
                int index = nomes.indexOf(nome);
                if (index == -1) {
                    nomes.add(nome);
                    contagens.add(1);
                } else {
                    contagens.set(index, contagens.get(index) + 1);
                }
            }
        }

        for (int i = 0; i < nomes.size(); i++) {
            if (contagens.get(i) > maxAtendimentos) {
                maxAtendimentos = contagens.get(i);
                profMaisDemandado = nomes.get(i);
            }
        }

        if (!profMaisDemandado.isEmpty()) {
            System.out.println(profMaisDemandado + " (" + maxAtendimentos + " atendimentos)");
        }

    }

}
