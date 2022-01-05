package br.com.alura.livraria.modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "livros")
public class CadastroLivros {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String titulo;
	
	@Column(name = "data_lancamento")
	private LocalDate dataLancamento;
	
	@Column(name = "numero_paginas")
	private Integer numeroPaginas;
	
	@ManyToOne
	private CadastroAutores cadastroAutor;
	
	@ManyToOne							
	private Usuario usuario;

	public CadastroLivros(String titulo, LocalDate dataLancamento, Integer numeroPaginas,
			CadastroAutores cadastroAutor, Usuario usuario) {
		
		this.titulo = titulo;
		this.dataLancamento = dataLancamento;
		this.numeroPaginas = numeroPaginas;
		this.cadastroAutor = cadastroAutor;
		this.usuario=usuario;
	}

	public void atualizarInformacoes(String titulo, LocalDate dataLancamento, Integer numeroPaginas) {
		this.titulo = titulo;
		this.dataLancamento = dataLancamento;
		this.numeroPaginas = numeroPaginas;
	}
	
	
	public boolean pertenceAoUsuario(Usuario usuario) {
		return this.usuario.equals(usuario);
	}
	
}
