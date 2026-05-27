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

/**
 * Repositório genérico para persistência em arquivos JSON via Gson.
 *
 * Usa adaptadores personalizados para serializar LocalDate e LocalTime,
 * pois o Gson padrão não suporta tipos java.time diretamente.
 */
public class JsonRepository {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setPrettyPrinting()
            .create();

    /**
     * Salva uma lista de objetos em um arquivo JSON.
     *
     * @param lista   lista de objetos a serializar
     * @param arquivo caminho do arquivo de destino (ex: "data/pacientes.json")
     * @param <T>     tipo dos elementos da lista
     */
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

    /**
     * Carrega uma lista de objetos a partir de um arquivo JSON.
     *
     * @param arquivo caminho do arquivo fonte
     * @param tipo    tipo genérico da lista (ex: {@code new TypeToken<List<Paciente>>(){}.getType()})
     * @param <T>     tipo dos elementos da lista
     * @return lista desserializada, ou lista vazia se o arquivo não existir
     */
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
