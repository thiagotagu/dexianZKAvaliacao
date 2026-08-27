package com.educzk.service;

import com.educzk.model.Aluno;
import com.educzk.repository.AlunoRepository;
import com.educzk.repository.PresencaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private PresencaRepository presencaRepository;

    private AlunoService service;

    @BeforeEach
    void setUp() {
        service = new AlunoService(alunoRepository, presencaRepository);
    }

    @Test
    void deveSalvarAlunoValidoRemovendoEspacos() {
        Aluno aluno = aluno("  Maria Silva  ", "  2026100  ", "  maria@escola.edu.br  ");
        when(alunoRepository.save(aluno)).thenReturn(aluno);

        Aluno salvo = service.salvar(aluno);

        assertEquals("Maria Silva", salvo.getNome());
        assertEquals("2026100", salvo.getMatricula());
        assertEquals("maria@escola.edu.br", salvo.getEmail());
        verify(alunoRepository).save(aluno);
    }

    @Test
    void naoDeveSalvarMatriculaDuplicada() {
        Aluno aluno = aluno("Maria Silva", "2026100", "maria@escola.edu.br");
        when(alunoRepository.existsByMatriculaIgnoreCaseAndIdNot("2026100", -1L)).thenReturn(true);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.salvar(aluno));

        assertEquals("Já existe um aluno com esta matrícula.", erro.getMessage());
        verify(alunoRepository, never()).save(aluno);
    }

    @Test
    void naoDeveSalvarEmailInvalido() {
        Aluno aluno = aluno("Maria Silva", "2026100", "email-invalido");

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.salvar(aluno));

        assertEquals("Informe um e-mail válido.", erro.getMessage());
        verify(alunoRepository, never()).save(aluno);
    }

    @Test
    void naoDeveExcluirAlunoComHistorico() {
        Aluno aluno = aluno("Maria Silva", "2026100", "maria@escola.edu.br");
        aluno.setId(1L);
        when(presencaRepository.existsByAlunoId(1L)).thenReturn(true);

        IllegalStateException erro = assertThrows(IllegalStateException.class,
                () -> service.excluir(aluno));

        assertEquals("O aluno possui histórico de presença e não pode ser excluído.", erro.getMessage());
        verify(alunoRepository, never()).delete(aluno);
    }

    private Aluno aluno(String nome, String matricula, String email) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setEmail(email);
        return aluno;
    }
}
