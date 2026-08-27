package com.educzk.service;

import com.educzk.model.Ambiente;
import com.educzk.model.TipoAmbiente;
import com.educzk.repository.AmbienteRepository;
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
class AmbienteServiceTest {

    @Mock
    private AmbienteRepository ambienteRepository;

    @Mock
    private PresencaRepository presencaRepository;

    private AmbienteService service;

    @BeforeEach
    void setUp() {
        service = new AmbienteService(ambienteRepository, presencaRepository);
    }

    @Test
    void deveSalvarAmbienteValido() {
        Ambiente ambiente = ambiente("  Laboratório  ", 20);
        when(ambienteRepository.save(ambiente)).thenReturn(ambiente);

        Ambiente salvo = service.salvar(ambiente);

        assertEquals("Laboratório", salvo.getNome());
        verify(ambienteRepository).save(ambiente);
    }

    @Test
    void naoDeveSalvarCapacidadeMenorQueOcupacaoAtual() {
        Ambiente ambiente = ambiente("Laboratório", 4);
        ambiente.setId(1L);
        when(presencaRepository.countByAmbienteIdAndSaidaIsNull(1L)).thenReturn(5L);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.salvar(ambiente));

        assertEquals("A capacidade não pode ser menor que a ocupação atual.", erro.getMessage());
        verify(ambienteRepository, never()).save(ambiente);
    }

    @Test
    void naoDeveSalvarCapacidadeInvalida() {
        Ambiente ambiente = ambiente("Laboratório", 0);

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.salvar(ambiente));

        assertEquals("A capacidade deve ser maior que zero.", erro.getMessage());
    }

    @Test
    void naoDeveExcluirAmbienteComHistorico() {
        Ambiente ambiente = ambiente("Laboratório", 20);
        ambiente.setId(1L);
        when(presencaRepository.existsByAmbienteId(1L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.excluir(ambiente));

        verify(ambienteRepository, never()).delete(ambiente);
    }

    private Ambiente ambiente(String nome, int capacidade) {
        Ambiente ambiente = new Ambiente();
        ambiente.setNome(nome);
        ambiente.setTipo(TipoAmbiente.LABORATORIO);
        ambiente.setCapacidade(capacidade);
        return ambiente;
    }
}
