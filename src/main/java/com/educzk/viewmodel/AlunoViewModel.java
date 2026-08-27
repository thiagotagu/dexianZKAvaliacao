package com.educzk.viewmodel;

import com.educzk.model.Aluno;
import com.educzk.service.AlunoService;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.util.List;

@VariableResolver(DelegatingVariableResolver.class)
public class AlunoViewModel extends BaseViewModel {

    @WireVariable
    private AlunoService alunoService;

    private List<Aluno> alunos;
    private Aluno aluno = new Aluno();

    @Init
    public void init() {
        recarregar();
    }

    @Command
    @NotifyChange({"alunos", "aluno"})
    public void salvar() {
        executar(() -> {
            alunoService.salvar(aluno);
            aluno = new Aluno();
            recarregar();
        }, "Aluno salvo.");
    }

    @Command
    @NotifyChange("aluno")
    public void editar(@BindingParam("item") Aluno item) {
        aluno = item;
    }

    @Command
    @NotifyChange({"alunos", "aluno"})
    public void excluir(@BindingParam("item") Aluno item) {
        executar(() -> {
            alunoService.excluir(item);
            aluno = new Aluno();
            recarregar();
        }, "Aluno excluído.");
    }

    @Command
    @NotifyChange("aluno")
    public void cancelar() {
        aluno = new Aluno();
    }

    private void recarregar() {
        alunos = alunoService.listar();
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public Aluno getAluno() {
        return aluno;
    }
}
