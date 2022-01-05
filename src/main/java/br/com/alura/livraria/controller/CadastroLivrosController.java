package br.com.alura.livraria.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.livraria.dto.AtualizacaoLivrosFormDto;
import br.com.alura.livraria.dto.CadastroLivrosDto;
import br.com.alura.livraria.dto.CadastroLivrosFormDto;
import br.com.alura.livraria.modelo.Usuario;
import br.com.alura.livraria.service.CadastroLivrosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/livros")
@Api(tags="Livros")
public class CadastroLivrosController {
	
	@Autowired
	private CadastroLivrosService service;
	
	
	@GetMapping
	@ApiOperation("Listar livros")
	public Page <CadastroLivrosDto> listar(@PageableDefault(size=15) Pageable paginacao,@ApiIgnore @AuthenticationPrincipal Usuario logado){
		return service.listar(paginacao,logado);
	}
	
	@PostMapping
	@ApiOperation("Cadastrar novo livro")
	public ResponseEntity<CadastroLivrosDto> cadastrar(@RequestBody @Valid CadastroLivrosFormDto dto,UriComponentsBuilder uriBuilder,@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		CadastroLivrosDto cadastroLivrosDto = service.cadastrar(dto,logado);
		URI uri = uriBuilder
				.path("/livros/{id}")
				.buildAndExpand(cadastroLivrosDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(cadastroLivrosDto);
	}
	
	
	@PutMapping
	@ApiOperation("Atualizar livro")
	public ResponseEntity<CadastroLivrosDto> atualizar(@RequestBody @Valid AtualizacaoLivrosFormDto dto,@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		CadastroLivrosDto atualizada = service.atualizar(dto,logado);
		return ResponseEntity.ok(atualizada);
	}
	
	@DeleteMapping("/{id}")
	@ApiOperation("Deletar livro")
	public ResponseEntity<CadastroLivrosDto> deletar(@PathVariable @NotNull Long id,@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		service.remover(id,logado);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Detalhar livro")
	public ResponseEntity<CadastroLivrosDto> detalhar(@PathVariable @NotNull Long id,@ApiIgnore @AuthenticationPrincipal Usuario logado) {
		CadastroLivrosDto dto = service.detalhar(id,logado);
		return ResponseEntity.ok(dto);
	}
	
}
