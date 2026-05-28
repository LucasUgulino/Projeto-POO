package com.clinica.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class JsonRepository {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setPrettyPrinting()
            .create();

 
    public static <T> void salvar(List<T> lista, String arquivo) {
        File file = new File(arquivo);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo: " + arquivo, e);
        }
    }

  
    public static <T> List<T> carregar(String arquivo, Type tipo) {
        File file = new File(arquivo);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Reader reader = new FileReader(file)) {
            List<T> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo: " + arquivo, e);
        }
    }

    /** Expõe o Gson configurado para uso externo quando necessário. */
    public static Gson getGson() {
        return gson;
    }
}
