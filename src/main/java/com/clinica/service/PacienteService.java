package com.clinica.service;

import java.util.ArrayList;

import com.clinica.model.Paciente;

public class PacienteService {

    private ArrayList<Paciente> pacientes;

    public PacienteService() {
        pacientes = new ArrayList<>();
    }

    public boolean cadastrar(Paciente paciente) {
        if (paciente == null) {
            return false;
        }

        if (pacientes.size() >= 10) {
            return false;
        }

        if (buscarPorCpf(paciente.getCpf()) != null) {
            return false;
        }

        pacientes.add(paciente);
        return true;
    }

    public Paciente buscarPorCpf(String cpf) {
        for (Paciente paciente : pacientes) {
            if (paciente.getCpf().equals(cpf)) {
                return paciente;
            }
        }

        return null;
    }

    public ArrayList<Paciente> listarAtivos() {
        return pacientes;
    }

    public boolean limiteDe10AtivosSimultaneos() {
        return pacientes.size() < 10;
    }
}
