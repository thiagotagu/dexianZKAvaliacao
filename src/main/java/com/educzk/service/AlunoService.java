package com.educzk.service;

import com.educzk.model.Aluno;
import com.educzk.repository.AlunoRepository;
import com.educzk.repository.PresencaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunos;
    private final PresencaRepository presencas;

    public List<Aluno> listar() {
        return alunos.findAll(Sort.by("nome"));
    }

    public List<Aluno> listarDisponiveisParaEntrada() {
        return alunos.findDisponiveisParaEntrada();
    }

    @Transactional
    public Aluno salvar(Aluno aluno) {
        aluno.setNome(obrigatorio(aluno.getNome(), "Informe o nome."));
        aluno.setMatricula(obrigatorio(aluno.getMatricula(), "Informe a matrícula."));
        aluno.setEmail(obrigatorio(aluno.getEmail(), "Informe o e-mail."));

        if (!aluno.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Informe um e-mail válido.");
        }

        long id = aluno.getId() == null ? -1L : aluno.getId();
        if (alunos.existsByMatriculaIgnoreCaseAndIdNot(aluno.getMatricula(), id)) {
            throw new IllegalArgumentException("Já existe um aluno com esta matrícula.");
        }

        return alunos.save(aluno);
    }

    @Transactional
    public void excluir(Aluno aluno) {
        if (presencas.existsByAlunoId(aluno.getId())) {
            throw new IllegalStateException("O aluno possui histórico de presença e não pode ser excluído.");
        }
        alunos.delete(aluno);
    }

    private String obrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor.trim();
    }
}
