package com.clinica.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Fila de Espera")
class FilaEsperaTest {

    @Test
    @DisplayName("Pacientes prioritários devem ser chamados antes dos pacientes normais")
    void deveChamarPrioritariosAntesDosNormais() {
        FilaEspera fila = new FilaEspera();
        Paciente normal = new Paciente("Ana", "111", "91911110000", 25, "Unimed", "normal");
        Paciente idoso = new Paciente("Carlos", "222", "91922220000", 70, "particular", "idoso");
        Paciente urgente = new Paciente("Bruna", "333", "91933330000", 40, "Bradesco", "urgente");

        fila.adicionar(normal);
        fila.adicionar(idoso);
        fila.adicionar(urgente);

        assertEquals(idoso, fila.proximo());
        assertEquals(urgente, fila.proximo());
        assertEquals(normal, fila.proximo());
    }

    @Test
    @DisplayName("Fila deve rejeitar paciente nulo e CPF duplicado")
    void deveAdicionarPacienteValidoEIgnorarPacienteNulo() {
        FilaEspera fila = new FilaEspera();
        Paciente paciente = new Paciente("Ana", "111", "91911110000", 25, "Unimed", "normal");
        
        fila.adicionar(null);
        fila.adicionar(paciente);
        
        assertEquals(1, fila.tamanho());
        assertEquals(paciente, fila.proximo());
    }

}
