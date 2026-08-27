package com.educzk.model;

public enum TipoAmbiente {
    SALA_DE_AULA("Sala de aula"),
    LABORATORIO("Laboratório"),
    SALA_DE_ESTUDOS("Sala de estudos");

    private final String descricao;

    TipoAmbiente(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
