package com.educzk.service;

import com.educzk.model.Aluno;
import com.educzk.model.Ambiente;
import com.educzk.model.Presenca;
import com.educzk.repository.PresencaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresencaServiceTest {

    @Mock
    private PresencaRepository presencaRepository;

    private PresencaService service;
    private Aluno aluno;
    private Ambiente ambiente;

    @BeforeEach
    void setUp() {
        service = new PresencaService(presencaRepository);
        aluno = new Aluno();
        aluno.setId(1L);
        ambiente = new Ambiente();
        ambiente.setId(2L);
        ambiente.setCapacidade(10);
    }

    @Test
    void deveRegistrarEntrada() {
        when(presencaRepository.findByAlunoIdAndSaidaIsNull(1L)).thenReturn(Optional.empty());
        when(presencaRepository.countByAmbienteIdAndSaidaIsNull(2L)).thenReturn(3L);
        ArgumentCaptor<Presenca> captor = ArgumentCaptor.forClass(Presenca.class);

        service.registrarEntrada(aluno, ambiente);

        verify(presencaRepository).save(captor.capture());
        Presenca salva = captor.getValue();
        assertEquals(aluno, salva.getAluno());
        assertEquals(ambiente, salva.getAmbiente());
        assertNotNull(salva.getEntrada());
    }

    @Test
    void naoDeveRegistrarAlunoComPresencaAtiva() {
        when(presencaRepository.findByAlunoIdAndSaidaIsNull(1L)).thenReturn(Optional.of(new Presenca()));

        IllegalStateException erro = assertThrows(IllegalStateException.class,
                () -> service.registrarEntrada(aluno, ambiente));

        assertEquals("Este aluno já está em um ambiente.", erro.getMessage());
        verify(presencaRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void naoDeveRegistrarEntradaEmAmbienteLotado() {
        when(presencaRepository.findByAlunoIdAndSaidaIsNull(1L)).thenReturn(Optional.empty());
        when(presencaRepository.countByAmbienteIdAndSaidaIsNull(2L)).thenReturn(10L);

        IllegalStateException erro = assertThrows(IllegalStateException.class,
                () -> service.registrarEntrada(aluno, ambiente));

        assertEquals("O ambiente atingiu sua capacidade máxima.", erro.getMessage());
        verify(presencaRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveRegistrarSaida() {
        Presenca presenca = new Presenca();
        presenca.setId(5L);
        when(presencaRepository.findById(5L)).thenReturn(Optional.of(presenca));

        service.registrarSaida(presenca);

        assertNotNull(presenca.getSaida());
    }

    @Test
    void naoDeveRegistrarSaidaDuasVezes() {
        Presenca presenca = new Presenca();
        presenca.setId(5L);
        presenca.setSaida(java.time.LocalDateTime.now());
        when(presencaRepository.findById(5L)).thenReturn(Optional.of(presenca));

        IllegalStateException erro = assertThrows(IllegalStateException.class,
                () -> service.registrarSaida(presenca));

        assertTrue(erro.getMessage().contains("já foi registrada"));
    }
}
