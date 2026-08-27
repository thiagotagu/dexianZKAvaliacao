package com.educzk.viewmodel;

import com.educzk.model.Ambiente;
import com.educzk.model.TipoAmbiente;
import com.educzk.service.AmbienteService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

@VariableResolver(DelegatingVariableResolver.class)
public class AmbienteViewModel extends BaseViewModel {

    @WireVariable
    private AmbienteService ambienteService;

    private List<Ambiente> ambientes;
    private Ambiente ambiente = novoAmbiente();

    @Init
    public void init() {
        recarregar();
    }

    @Command
    @NotifyChange({"ambientes", "ambiente"})
    public void salvar() {
        executar(() -> {
            ambienteService.salvar(ambiente);
            ambiente = novoAmbiente();
            recarregar();
        }, "Ambiente salvo.");
    }

    @Command
    @NotifyChange("ambiente")
    public void editar(@BindingParam("item") Ambiente item) {
        ambiente = item;
    }

    @Command
    @NotifyChange({"ambientes", "ambiente"})
    public void excluir(@BindingParam("item") Ambiente item) {
        executar(() -> {
            ambienteService.excluir(item);
            ambiente = novoAmbiente();
            recarregar();
        }, "Ambiente excluído.");
    }

    @Command
    @NotifyChange("ambiente")
    public void cancelar() {
        ambiente = novoAmbiente();
    }

    private void recarregar() {
        ambientes = ambienteService.listar();
    }

    private Ambiente novoAmbiente() {
        Ambiente novo = new Ambiente();
        novo.setTipo(TipoAmbiente.SALA_DE_AULA);
        novo.setCapacidade(30);
        return novo;
    }

    public List<Ambiente> getAmbientes() {
        return ambientes;
    }

    public Ambiente getAmbiente() {
        return ambiente;
    }

    public TipoAmbiente[] getTipos() {
        return TipoAmbiente.values();
    }
}
