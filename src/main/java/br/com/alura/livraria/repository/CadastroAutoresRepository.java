package br.com.alura.livraria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.livraria.modelo.CadastroAutores;

public interface CadastroAutoresRepository extends JpaRepository<CadastroAutores, Long>{

}
