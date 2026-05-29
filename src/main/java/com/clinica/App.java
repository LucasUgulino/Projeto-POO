package com.clinica;

import com.clinica.model.*;
import com.clinica.report.RelatorioService;
import com.clinica.service.*;
import com.clinica.util.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {

    static Scanner scanner = new Scanner(System.in);
    static PacienteService pacienteService = new PacienteService();
    static ProfissionalService profissionalService = new ProfissionalService();
    static FilaEspera filaEspera = new FilaEspera();
    static AgendamentoService agendamentoService = new AgendamentoService(filaEspera);
    static FaturamentoService faturamentoService = new FaturamentoService();

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            opcao = lerInt("Opção: ");
            switch (opcao) {
                case 1 -> cadastrarPaciente();
                case 2 -> cadastrarProfissional();
                case 3 -> agendarConsulta();
                case 4 -> agendarProcedimento();
                case 5 -> cancelarAgendamento();
                case 6 -> finalizarAtendimento();
                case 7 -> verFilaEspera();
                case 8 -> listarPorProfissional();
                case 9 -> relatorioMensal();
                case 0 -> System.out.println("Encerrando sistema. Até logo!");
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    static void exibirMenu() {
        System.out.println("""
                \n===== CLÍNICA AGENDAMENTOS =====
                1. Cadastrar paciente
                2. Cadastrar profissional
                3. Agendar consulta
                4. Agendar procedimento
                5. Cancelar agendamento
                6. Finalizar atendimento (gera recibo)
                7. Ver fila de espera
                8. Listar agendamentos por profissional
                9. Relatório mensal
                0. Sair
                ================================""");
    }

    static void cadastrarPaciente() {
        System.out.println("\n-- Cadastrar Paciente --");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        String contato = lerTexto("Contato: ");
        int idade = lerInt("Idade: ");
        String convenio = lerTexto("Convênio (ou 'particular'): ");
        String prioridade = lerTexto("Prioridade (normal/idoso/urgente): ");

        Paciente p = new Paciente(nome, cpf, contato, idade, convenio, prioridade);
        boolean ok = pacienteService.cadastrar(p);
        System.out.println(ok ? "Paciente cadastrado!" : "Erro: limite atingido ou CPF duplicado.");
    }

    static void cadastrarProfissional() {
        System.out.println("\n-- Cadastrar Profissional --");
        String nome = lerTexto("Nome: ");
        String cpf = lerTexto("CPF: ");
        String contato = lerTexto("Contato: ");
        String especialidade = lerTexto("Especialidade: ");
        double valor = lerDouble("Valor da consulta: ");
        int duracao = lerInt("Duração do atendimento (min): ");

        Profissional prof = new Profissional(nome, cpf, contato, especialidade, valor, duracao);
        profissionalService.cadastrar(prof);
        System.out.println("Profissional cadastrado!");
    }

    static void agendarConsulta() {
        System.out.println("\n-- Agendar Consulta --");
        Paciente paciente = buscarPaciente();
        if (paciente == null) return;
        Profissional prof = buscarProfissional();
        if (prof == null) return;

        Sala sala = lerSala();
        LocalDate data = lerData("Data (dd/MM/yyyy): ");
        LocalTime hora = lerHora("Hora (HH:mm): ");
        String tipo = lerTexto("Tipo (NORMAL/RETORNO): ").toUpperCase();

        Consulta c = new Consulta(paciente, prof, sala, data, hora, StatusAgendamento.AGENDADO, tipo);
        boolean ok = agendamentoService.agendar(c);
        System.out.println(ok ? "Consulta agendada!" : "Erro: conflito de horário.");
    }

    static void agendarProcedimento() {
        System.out.println("\n-- Agendar Procedimento --");
        Paciente paciente = buscarPaciente();
        if (paciente == null) return;
        Profissional prof = buscarProfissional();
        if (prof == null) return;

        Sala sala = lerSala();
        LocalDate data = lerData("Data (dd/MM/yyyy): ");
        LocalTime hora = lerHora("Hora (HH:mm): ");
        String descricao = lerTexto("Descrição do procedimento: ");
        double valor = lerDouble("Valor: ");

        Procedimento proc = new Procedimento(paciente, prof, sala, data, hora,
                StatusAgendamento.AGENDADO, descricao, valor);
        boolean ok = agendamentoService.agendar(proc);
        System.out.println(ok ? "Procedimento agendado!" : "Erro: conflito de horário.");
    }

    static void cancelarAgendamento() {
        System.out.println("\n-- Cancelar Agendamento --");
        int id = lerInt("ID do agendamento: ");
        String motivo = lerTexto("Motivo: ");
        double taxa = agendamentoService.cancelar(id, motivo);
        if (taxa >= 0) {
            System.out.printf("Cancelado. Taxa cobrada: R$ %.2f%n", taxa);
        } else {
            System.out.println("Agendamento não encontrado.");
        }
    }

    static void finalizarAtendimento() {
        System.out.println("\n-- Finalizar Atendimento --");
        int id = lerInt("ID do agendamento: ");
        boolean ok = agendamentoService.finalizar(id);
        if (!ok) {
            System.out.println("Agendamento não encontrado ou já cancelado.");
            return;
        }
        Agendamento ag = agendamentoService.buscarPorId(id);
        Recibo recibo = faturamentoService.gerarRecibo(ag);
        imprimirRecibo(recibo);
    }

    static void verFilaEspera() {
        System.out.println("\n-- Fila de Espera --");
        if (filaEspera.estaVazia()) {
            System.out.println("Fila vazia.");
            return;
        }
        List<Paciente> lista = filaEspera.getFila();
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("%d. %s [%s]%n", i + 1, lista.get(i).getNome(), lista.get(i).getPrioridade());
        }
    }

    static void listarPorProfissional() {
        System.out.println("\n-- Agendamentos por Profissional --");
        Profissional prof = buscarProfissional();
        if (prof == null) return;

        RelatorioService rel = new RelatorioService(agendamentoService.listarTodos());
        List<Agendamento> lista = rel.listarPorProfissional(prof);

        if (lista.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (int i = 0; i < lista.size(); i++) {
            Agendamento a = lista.get(i);
            System.out.printf("[%d] %s — %s %s — %s%n",
                    i, a.getPaciente().getNome(),
                    a.getData().format(fmt), a.getHora(),
                    a.getStatus());
        }
    }

    static void relatorioMensal() {
        System.out.println("\n-- Relatório Mensal --");
        int mes = lerInt("Mês (1-12): ");
        int ano = lerInt("Ano: ");
        RelatorioService rel = new RelatorioService(agendamentoService.listarTodos());
        rel.relatorioMensal(mes, ano);
    }

    // ---- Helpers ----

    static Paciente buscarPaciente() {
        String cpf = lerTexto("CPF do paciente: ");
        Paciente p = pacienteService.buscarPorCpf(cpf);
        if (p == null) System.out.println("Paciente não encontrado.");
        return p;
    }

    static Profissional buscarProfissional() {
        String esp = lerTexto("Especialidade do profissional: ");
        Profissional prof = profissionalService.buscarPorEspecialidade(esp);
        if (prof == null) System.out.println("Profissional não encontrado.");
        return prof;
    }

    static Sala lerSala() {
        int num = lerInt("Número da sala: ");
        String nome = lerTexto("Nome da sala: ");
        String tipo = lerTexto("Tipo da sala: ");
        return new Sala(num, nome, tipo);
    }

    static void imprimirRecibo(Recibo recibo) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n=== RECIBO DE ATENDIMENTO ===");
        System.out.printf("Paciente     : %s (CPF: %s)%n", recibo.getPaciente().getNome(), recibo.getPaciente().getCpf());
        System.out.printf("Profissional : %s — %s%n", recibo.getProfissional().getNome(), recibo.getProfissional().getEspecialidade());
        System.out.printf("Data         : %s%n", recibo.getData().format(fmt));
        System.out.printf("Serviço      : %s%n", recibo.getServico());
        System.out.printf("Convênio     : %s%n", recibo.getPaciente().getConvenio());
        System.out.printf("Valor        : R$ %.2f%n", recibo.getTotalCobrado());
        System.out.println("=============================\n");
    }

    static String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    static int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scanner.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    static double lerDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("Digite um valor numérico válido.");
            }
        }
    }

    static LocalDate lerData(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use dd/MM/yyyy.");
            }
        }
    }

    static LocalTime lerHora(String prompt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        while (true) {
            try {
                System.out.print(prompt);
                return LocalTime.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use HH:mm.");
            }
        }
    }
}