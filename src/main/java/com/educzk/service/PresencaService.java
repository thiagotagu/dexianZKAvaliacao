package com.educzk.service;

import com.educzk.model.Aluno;
import com.educzk.model.Ambiente;
import com.educzk.model.Presenca;
import com.educzk.repository.PresencaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresencaService {

    private final PresencaRepository presencas;

    public List<Presenca> listarAtivas() {
        return presencas.findBySaidaIsNullOrderByEntradaDesc();
    }

    public long ocupacao(Ambiente ambiente) {
        return presencas.countByAmbienteIdAndSaidaIsNull(ambiente.getId());
    }

    @Transactional
    public void registrarEntrada(Aluno aluno, Ambiente ambiente) {
        if (aluno == null || ambiente == null) {
            throw new IllegalArgumentException("Selecione o aluno e o ambiente.");
        }
        if (presencas.findByAlunoIdAndSaidaIsNull(aluno.getId()).isPresent()) {
            throw new IllegalStateException("Este aluno já está em um ambiente.");
        }
        if (ocupacao(ambiente) >= ambiente.getCapacidade()) {
            throw new IllegalStateException("O ambiente atingiu sua capacidade máxima.");
        }

        Presenca presenca = new Presenca();
        presenca.setAluno(aluno);
        presenca.setAmbiente(ambiente);
        presenca.setEntrada(LocalDateTime.now());
        presencas.save(presenca);
    }

    @Transactional
    public void registrarSaida(Presenca presenca) {
        Presenca atual = presencas.findById(presenca.getId()).orElseThrow();
        if (atual.getSaida() != null) {
            throw new IllegalStateException("A saída já foi registrada.");
        }
        atual.setSaida(LocalDateTime.now());
    }
}
