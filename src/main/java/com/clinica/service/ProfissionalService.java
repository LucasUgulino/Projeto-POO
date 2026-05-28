package com.clinica.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import com.clinica.model.Profissional;

public class ProfissionalService {

    private ArrayList<Profissional> profissionais;

    public ProfissionalService() {
        profissionais = new ArrayList<>();
    }

    public boolean cadastrar(Profissional profissional) {
        if (profissional == null) {
            return false;
        }

        profissionais.add(profissional);
        return true;
    }

    public Profissional buscarPorEspecialidade(String especialidade) {
        for (Profissional profissional : profissionais) {
            if (profissional.getEspecialidade().equalsIgnoreCase(especialidade)) {
                return profissional;
            }
        }

        return null;
    }

    public ArrayList<Profissional> listarDisponiveis(LocalDateTime dataHora) {
        ArrayList<Profissional> disponiveis = new ArrayList<>();
        LocalTime hora = dataHora.toLocalTime();

        for (Profissional profissional : profissionais) {
            if (profissional.getHorarios().contains(hora)) {
                disponiveis.add(profissional);
            }
        }

        return disponiveis;
    }

    public ArrayList<Profissional> listarTodos() {
        return profissionais;
    }
}
