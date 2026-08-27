package com.educzk.repository;

import com.educzk.model.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    Optional<Presenca> findByAlunoIdAndSaidaIsNull(Long alunoId);

    long countByAmbienteIdAndSaidaIsNull(Long ambienteId);

    List<Presenca> findBySaidaIsNullOrderByEntradaDesc();

    boolean existsByAlunoId(Long alunoId);

    boolean existsByAmbienteId(Long ambienteId);
}
