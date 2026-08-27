package com.educzk.service;

import com.educzk.model.Ambiente;
import com.educzk.repository.AmbienteRepository;
import com.educzk.repository.PresencaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AmbienteService {

    private final AmbienteRepository ambientes;
    private final PresencaRepository presencas;

    public List<Ambiente> listar() {
        return ambientes.findAll(Sort.by("nome"));
    }

    @Transactional
    public Ambiente salvar(Ambiente ambiente) {
        if (ambiente.getNome() == null || ambiente.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome do ambiente.");
        }
        if (ambiente.getTipo() == null) {
            throw new IllegalArgumentException("Selecione o tipo do ambiente.");
        }
        if (ambiente.getCapacidade() == null || ambiente.getCapacidade() < 1) {
            throw new IllegalArgumentException("A capacidade deve ser maior que zero.");
        }

        long ocupacaoAtual = ambiente.getId() == null
                ? 0
                : presencas.countByAmbienteIdAndSaidaIsNull(ambiente.getId());
        if (ambiente.getCapacidade() < ocupacaoAtual) {
            throw new IllegalArgumentException("A capacidade não pode ser menor que a ocupação atual.");
        }

        ambiente.setNome(ambiente.getNome().trim());
        return ambientes.save(ambiente);
    }

    @Transactional
    public void excluir(Ambiente ambiente) {
        if (presencas.existsByAmbienteId(ambiente.getId())) {
            throw new IllegalStateException("O ambiente possui histórico e não pode ser excluído.");
        }
        ambientes.delete(ambiente);
    }
}
