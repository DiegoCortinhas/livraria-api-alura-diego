package br.com.alura.livraria.controller;

import java.net.URI;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.livraria.dto.AtualizacaoAutoresFormDto;
import br.com.alura.livraria.dto.AtualizacaoLivrosFormDto;
import br.com.alura.livraria.dto.CadastroAutoresDto;
import br.com.alura.livraria.dto.CadastroAutoresFormDto;
import br.com.alura.livraria.dto.CadastroLivrosDto;
import br.com.alura.livraria.service.CadastroAutoresService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/autores")
@Api(tags="Autores")
public class CadastroAutoresController {
	
	@Autowired
	private CadastroAutoresService service;
	
	@GetMapping
	@ApiOperation("Listar autores")
	public Page <CadastroAutoresDto> listar(@PageableDefault(size=15) Pageable paginacao){
		return service.listar(paginacao);
	}
	
	@PostMapping
	@ApiOperation("Cadastrar novo autor")
	public ResponseEntity<CadastroAutoresDto>  cadastrar(@RequestBody @Valid CadastroAutoresFormDto dto,UriComponentsBuilder uriBuilder) {
		CadastroAutoresDto cadastroAutoresDto = service.cadastrar(dto);
		URI uri = uriBuilder
				.path("/autores/{id}")
				.buildAndExpand(cadastroAutoresDto.getId())
				.toUri();
		return ResponseEntity.created(uri).body(cadastroAutoresDto);
	}
	
	@PutMapping
	@ApiOperation("Atualizar autor")
	public ResponseEntity<CadastroAutoresDto> atualizar(@RequestBody @Valid AtualizacaoAutoresFormDto dto) {
		CadastroAutoresDto atualizada = service.atualizar(dto);
		return ResponseEntity.ok(atualizada);
	}
	
	
	@DeleteMapping("/{id}")
	@ApiOperation("Deletar autor")
	public ResponseEntity<CadastroAutoresDto> deletar(@PathVariable @NotNull Long id) {
		service.remover(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{id}")
	@ApiOperation("Detalhar autor")
	public ResponseEntity<CadastroAutoresDto> detalhar(@PathVariable @NotNull Long id) {
		CadastroAutoresDto dto = service.detalhar(id);
		return ResponseEntity.ok(dto);
	}
	
}
