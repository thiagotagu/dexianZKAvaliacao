package com.educzk.repository;

import com.educzk.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    boolean existsByMatriculaIgnoreCaseAndIdNot(String matricula, Long id);

    @Query("""
            select aluno
            from Aluno aluno
            where not exists (
                select presenca.id
                from Presenca presenca
                where presenca.aluno = aluno
                  and presenca.saida is null
            )
            order by aluno.nome
            """)
    List<Aluno> findDisponiveisParaEntrada();
}
