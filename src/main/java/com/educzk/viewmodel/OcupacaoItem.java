package com.educzk.viewmodel;

import com.educzk.model.Ambiente;

public record OcupacaoItem(Ambiente ambiente, long ocupacao) {

    public int percentual() {
        return ambiente.getCapacidade() == 0
                ? 0
                : (int) Math.round(ocupacao * 100.0 / ambiente.getCapacidade());
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public String getResumo() {
        return ocupacao + " / " + ambiente.getCapacidade();
    }

    public int getPercentual() {
        return percentual();
    }

    public String getPercentualTexto() {
        return percentual() + "% ocupado";
    }
}
