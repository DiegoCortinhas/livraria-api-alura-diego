package br.com.alura.livraria.service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.livraria.dto.AtualizacaoAutoresFormDto;
import br.com.alura.livraria.dto.CadastroAutoresDto;
import br.com.alura.livraria.dto.CadastroAutoresFormDto;
import br.com.alura.livraria.dto.CadastroLivrosDto;
import br.com.alura.livraria.modelo.CadastroAutores;
import br.com.alura.livraria.modelo.CadastroLivros;
import br.com.alura.livraria.repository.CadastroAutoresRepository;

@Service
public class CadastroAutoresService {
	
	@Autowired
	private CadastroAutoresRepository cadastroAutoresRepository;
	
	private ModelMapper modelMapper = new ModelMapper();
		
	public Page<CadastroAutoresDto> listar(Pageable paginacao) {
		Page<CadastroAutores> cadastroAutores = cadastroAutoresRepository.findAll(paginacao);
		
		return cadastroAutores
				.map(c -> modelMapper.map(c, CadastroAutoresDto.class));
		
	}

	@Transactional
	public CadastroAutoresDto cadastrar(@Valid CadastroAutoresFormDto dto) {
		CadastroAutores cadastro = modelMapper.map(dto,CadastroAutores.class);
		cadastro.setId(null);
		cadastroAutoresRepository.save(cadastro);
		return modelMapper.map(cadastro,CadastroAutoresDto.class);
	}
	
	@Transactional
	public CadastroAutoresDto atualizar(@Valid AtualizacaoAutoresFormDto dto) {
		CadastroAutores autor = cadastroAutoresRepository.getById(dto.getId());
		autor.atualizarInformacoes(dto.getNome(),dto.getDataNascimento(),dto.getEmail(),dto.getMiniCurriculo());
		
		return modelMapper.map(autor, CadastroAutoresDto.class);
	}
	
	@Transactional
	public void remover(Long id) {
		cadastroAutoresRepository.deleteById(id);
	}

	public CadastroAutoresDto detalhar(Long id) {
		CadastroAutores autor = cadastroAutoresRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		return modelMapper.map(autor, CadastroAutoresDto.class);
	}
	
}
