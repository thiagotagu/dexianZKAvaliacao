package com.educzk.viewmodel;

import com.educzk.model.Aluno;
import com.educzk.model.Ambiente;
import com.educzk.model.Presenca;
import com.educzk.service.AlunoService;
import com.educzk.service.AmbienteService;
import com.educzk.service.PresencaService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@VariableResolver(DelegatingVariableResolver.class)
public class OcupacaoViewModel extends BaseViewModel {

    @WireVariable
    private AlunoService alunoService;

    @WireVariable
    private AmbienteService ambienteService;

    @WireVariable
    private PresencaService presencaService;

    private List<Aluno> alunosDisponiveis;
    private List<Ambiente> ambientes;
    private List<Presenca> presencas;
    private List<OcupacaoItem> ocupacoes;
    private Aluno alunoEntrada;
    private Ambiente ambienteEntrada;

    @Init
    public void init() {
        recarregar();
    }

    @Command
    @NotifyChange({"presencas", "ocupacoes", "alunosDisponiveis", "alunoEntrada", "ambienteEntrada"})
    public void registrarEntrada() {
        executar(() -> {
            presencaService.registrarEntrada(alunoEntrada, ambienteEntrada);
            alunoEntrada = null;
            ambienteEntrada = null;
            recarregar();
        }, "Entrada registrada.");
    }

    @Command
    @NotifyChange({"presencas", "ocupacoes", "alunosDisponiveis"})
    public void registrarSaida(@BindingParam("item") Presenca item) {
        executar(() -> {
            presencaService.registrarSaida(item);
            recarregar();
        }, "Saída registrada.");
    }

    private void recarregar() {
        alunosDisponiveis = alunoService.listarDisponiveisParaEntrada();
        ambientes = ambienteService.listar();
        presencas = presencaService.listarAtivas();
        ocupacoes = ambientes.stream()
                .map(item -> new OcupacaoItem(item, presencaService.ocupacao(item)))
                .toList();
    }

    public String formatar(LocalDateTime data) {
        return data == null ? "—" : data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public List<Aluno> getAlunosDisponiveis() { return alunosDisponiveis; }

    public List<Ambiente> getAmbientes() { return ambientes; }

    public List<Presenca> getPresencas() { return presencas; }

    public List<OcupacaoItem> getOcupacoes() { return ocupacoes; }

    public Aluno getAlunoEntrada() { return alunoEntrada; }

    public void setAlunoEntrada(Aluno alunoEntrada) { this.alunoEntrada = alunoEntrada; }

    public Ambiente getAmbienteEntrada() { return ambienteEntrada; }

    public void setAmbienteEntrada(Ambiente ambienteEntrada) { this.ambienteEntrada = ambienteEntrada; }
}
