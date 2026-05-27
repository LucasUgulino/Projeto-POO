package com.clinica.service;

import com.clinica.model.Consulta;
import com.clinica.model.FilaEspera;
import com.clinica.model.Paciente;
import com.clinica.model.Procedimento;
import com.clinica.model.Profissional;
import com.clinica.model.Sala;
import com.clinica.repository.JsonRepository;
import com.clinica.util.StatusAgendamento;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes dos Serviços e Persistência JSON da Clínica")
class ClinicaServiceTest {

    private Paciente paciente;
    private Profissional profissional;
    private Sala sala;

    private AgendamentoService agendamentoService;
    private FaturamentoService faturamentoService;
    private TaxaCancelamento taxaCancelamento;

    @BeforeEach
    void setUp() {
        paciente = new Paciente("Maria Silva", "123.456.789-00", "91999990000", 35, "Unimed", "normal");
        profissional = new Profissional("Dr. Carlos Souza", "987.654.321-00", "91988880000", "Cardiologia", 250.0, 30);
        sala = new Sala(1, "Consultório 1", "consulta");

        agendamentoService = new AgendamentoService();
        faturamentoService = new FaturamentoService();
        taxaCancelamento   = new TaxaCancelamento();
    }

    // -----------------------------------------------------------------------
    // Teste 1 — Conflito de horário
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Deve lançar exceção ao agendar dois pacientes no mesmo profissional/hora")
    void deveImpedirConflitoDeHorario() {
        LocalDate data = LocalDate.of(2026, 5, 28);
        LocalTime hora = LocalTime.of(14, 30);

        Consulta primeira = new Consulta(paciente, profissional, sala, data, hora,
                StatusAgendamento.AGENDADO, "NORMAL");
        agendamentoService.agendar(primeira);

        Paciente outroPaciente = new Paciente("João Costa", "111.222.333-44",
                "91977770000", 40, "particular", "normal");
        Consulta segunda = new Consulta(outroPaciente, profissional, sala, data, hora,
                StatusAgendamento.AGENDADO, "NORMAL");

        assertThrows(IllegalStateException.class,
                () -> agendamentoService.agendar(segunda),
                "Deve lançar IllegalStateException por conflito de horário");
    }

    // -----------------------------------------------------------------------
    // Teste 2 — Valor de retorno = 50% do valor base
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Consulta de retorno deve custar 50% do valor base do profissional")
    void deveCalcularValorRetorno() {
        Consulta retorno = new Consulta(paciente, profissional, sala,
                LocalDate.of(2026, 5, 28), LocalTime.of(10, 0),
                StatusAgendamento.AGENDADO, "RETORNO");

        double valorCalculado = faturamentoService.calcularValor(retorno);
        double valorEsperado  = profissional.getValorConsulta() * 0.50; // 125.0

        assertEquals(valorEsperado, valorCalculado, 0.001,
                "Retorno deve custar exatamente 50% do valor base");
    }

    // -----------------------------------------------------------------------
    // Teste 3 — Taxa de cancelamento fora do prazo (< 24 h)
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Cancelamento com menos de 24h de antecedência deve gerar taxa de 20%")
    void deveCobrarTaxaDeCancelamentoForaDoPrazo() {
        LocalDate dataConsulta  = LocalDate.of(2026, 5, 28);
        LocalTime horaConsulta  = LocalTime.of(14, 0);

        Consulta consulta = new Consulta(paciente, profissional, sala, dataConsulta, horaConsulta,
                StatusAgendamento.AGENDADO, "NORMAL");

        // Cancelando com apenas 2 horas de antecedência → deve gerar taxa
        LocalDate dataCancelamento = LocalDate.of(2026, 5, 28);
        LocalTime horaCancelamento = LocalTime.of(12, 0);

        double taxa = taxaCancelamento.calcularTaxa(consulta, dataCancelamento, horaCancelamento);
        double taxaEsperada = profissional.getValorConsulta() * 0.20; // 50.0

        assertTrue(taxa > 0, "Taxa deve ser maior que zero");
        assertEquals(taxaEsperada, taxa, 0.001, "Taxa deve ser 20% do valor base");
    }

    // -----------------------------------------------------------------------
    // Teste 4 — Próximo da fila ao cancelar
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Ao cancelar, o próximo paciente da fila deve ser retornado para ocupar o horário")
    void deveMoverPacienteDaFilaAoCancelar() {
        Paciente pacienteFila = new Paciente("Pedro Oliveira", "555.666.777-88",
                "91955550000", 28, "particular", "normal");

        FilaEspera fila = new FilaEspera();
        fila.adicionar(pacienteFila);

        AgendamentoService service = new AgendamentoService(fila);

        Consulta consulta = new Consulta(paciente, profissional, sala,
                LocalDate.of(2026, 5, 28), LocalTime.of(14, 30),
                StatusAgendamento.AGENDADO, "NORMAL");
        service.agendar(consulta);

        Paciente proximoDaFila = service.cancelar(consulta, "Paciente desistiu");

        assertEquals(StatusAgendamento.CANCELADO, consulta.getStatus(),
                "Status do agendamento deve ser CANCELADO");
        assertNotNull(proximoDaFila,
                "Deve retornar o próximo paciente da fila para ocupar o horário");
        assertEquals(pacienteFila.getNome(), proximoDaFila.getNome(),
                "O paciente retornado deve ser o primeiro da fila de espera");
    }

    // -----------------------------------------------------------------------
    // Teste 5 — Salvar e carregar JSON
    // -----------------------------------------------------------------------
    @Test
    @DisplayName("Deve salvar lista em JSON e recarregar com os mesmos dados")
    void deveSalvarECarregarJSON() {
        String arquivo = "target/test-data/pacientes_test.json";

        List<Paciente> original = List.of(
                new Paciente("Ana Costa",   "999.888.777-66", "91944440000", 25, "Bradesco",   "normal"),
                new Paciente("Rui Mendes",  "111.000.999-88", "91933330000", 45, "particular", "idoso")
        );

        JsonRepository.salvar(original, arquivo);

        Type tipo = new TypeToken<List<Paciente>>() {}.getType();
        List<Paciente> carregado = JsonRepository.carregar(arquivo, tipo);

        assertEquals(original.size(), carregado.size(),
                "Tamanho da lista carregada deve ser igual à original");
        assertEquals(original.get(0).getNome(), carregado.get(0).getNome(),
                "Nome do primeiro paciente deve ser preservado");
        assertEquals(original.get(1).getCpf(), carregado.get(1).getCpf(),
                "CPF do segundo paciente deve ser preservado");

        new File(arquivo).delete(); // limpeza após o teste
    }
}
