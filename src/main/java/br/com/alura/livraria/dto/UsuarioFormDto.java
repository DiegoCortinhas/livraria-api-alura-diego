package br.com.alura.livraria.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioFormDto {
	
	@NotBlank(message="Nome do usuario deve ser informado.")
	private String nome;
	
	@NotBlank
	private String login;
	
	@NotNull
	@JsonAlias("perfil_id")
	private Long perfilId;
	
		
}