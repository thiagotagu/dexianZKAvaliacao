package com.educzk.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "alunos", uniqueConstraints = @UniqueConstraint(columnNames = "matricula"))
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 30)
    private String matricula;

    @Column(nullable = false, length = 150)
    private String email;

}
