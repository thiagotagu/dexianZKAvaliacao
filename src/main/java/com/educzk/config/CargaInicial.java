package com.educzk.config;

import com.educzk.model.Aluno;
import com.educzk.model.Ambiente;
import com.educzk.model.TipoAmbiente;
import com.educzk.model.Usuario;
import com.educzk.repository.AlunoRepository;
import com.educzk.repository.AmbienteRepository;
import com.educzk.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CargaInicial implements CommandLineRunner {

    private final AlunoRepository alunoRepository;
    private final AmbienteRepository ambienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.findByLoginIgnoreCase("admin").isEmpty()) {
            Usuario administrador = new Usuario();
            administrador.setLogin("admin");
            administrador.setSenha(passwordEncoder.encode("admin"));
            usuarioRepository.save(administrador);
        }

        if (alunoRepository.count() == 0) {
            alunoRepository.saveAll(List.of(
                    aluno("Ana Souza", "2026001", "ana.souza@escola.edu.br"),
                    aluno("Bruno Martins", "2026002", "bruno.martins@escola.edu.br"),
                    aluno("Camila Oliveira", "2026003", "camila.oliveira@escola.edu.br"),
                    aluno("Daniel Costa", "2026004", "daniel.costa@escola.edu.br"),
                    aluno("Eduarda Lima", "2026005", "eduarda.lima@escola.edu.br"),
                    aluno("Felipe Rocha", "2026006", "felipe.rocha@escola.edu.br"),
                    aluno("Gabriela Alves", "2026007", "gabriela.alves@escola.edu.br"),
                    aluno("Henrique Melo", "2026008", "henrique.melo@escola.edu.br"),
                    aluno("Isabela Ribeiro", "2026009", "isabela.ribeiro@escola.edu.br"),
                    aluno("João Ferreira", "2026010", "joao.ferreira@escola.edu.br")
            ));
        }

        if (ambienteRepository.count() == 0) {
            ambienteRepository.saveAll(List.of(
                    ambiente("Sala 101", TipoAmbiente.SALA_DE_AULA, 35),
                    ambiente("Laboratório de Informática", TipoAmbiente.LABORATORIO, 24),
                    ambiente("Sala de Estudos", TipoAmbiente.SALA_DE_ESTUDOS, 18)
            ));
        }
    }

    private Aluno aluno(String nome, String matricula, String email) {
        Aluno aluno = new Aluno();
        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setEmail(email);
        return aluno;
    }

    private Ambiente ambiente(String nome, TipoAmbiente tipo, int capacidade) {
        Ambiente ambiente = new Ambiente();
        ambiente.setNome(nome);
        ambiente.setTipo(tipo);
        ambiente.setCapacidade(capacidade);
        return ambiente;
    }
}
