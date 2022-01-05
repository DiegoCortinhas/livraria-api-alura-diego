package br.com.alura.livraria.service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.alura.livraria.dto.AtualizacaoLivrosFormDto;
import br.com.alura.livraria.dto.CadastroLivrosDto;
import br.com.alura.livraria.dto.CadastroLivrosFormDto;
import br.com.alura.livraria.modelo.CadastroAutores;
import br.com.alura.livraria.modelo.CadastroLivros;
import br.com.alura.livraria.modelo.Usuario;
import br.com.alura.livraria.repository.CadastroAutoresRepository;
import br.com.alura.livraria.repository.CadastroLivrosRepository;
import br.com.alura.livraria.repository.UsuarioRepository;

@Service
public class CadastroLivrosService {
	
	@Autowired
	private CadastroLivrosRepository cadastroLivrosRepository;
	
	@Autowired
	private CadastroAutoresRepository cadastroAutoresRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public Page<CadastroLivrosDto> listar(Pageable paginacao,Usuario usuario) {
		
		return cadastroLivrosRepository.
				findAllByUsuario(paginacao,usuario)
				.map(t -> modelMapper.map(t, CadastroLivrosDto.class));
		
		/*
		 * Page<CadastroLivros> cadastroLivros =
		 * cadastroLivrosRepository.findAll(paginacao); return cadastroLivros.map(c ->
		 * modelMapper.map(c, CadastroLivrosDto.class));
		 */
	}
	
	@Transactional
	public CadastroLivrosDto cadastrar(CadastroLivrosFormDto dto, Usuario logado) {
		//throw new NullPointerException("testando Erro NullPointer");
		
		Long idUsuario = dto.getUsuarioId();
		Long idAutor = dto.getCadastroAutorId();
		
		try {
			CadastroAutores autor = cadastroAutoresRepository.getById(idAutor);
			Usuario usuario = usuarioRepository.getById(idUsuario);
			
			if(!usuario.equals(logado)) {
				lancarErroAcessoNegado();
			}
			
			CadastroLivros cadastro = modelMapper.map(dto, CadastroLivros.class);
			cadastro.setId(null);
			cadastro.setCadastroAutor(autor);
			cadastro.setUsuario(usuario);
			
			cadastroLivrosRepository.save(cadastro);
			return modelMapper.map(cadastro, CadastroLivrosDto.class);
			
		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException("autor inexistente");
		}

		
		 
	}
	
	@Transactional
	public CadastroLivrosDto atualizar(AtualizacaoLivrosFormDto dto, Usuario logado) {
		CadastroLivros livro = cadastroLivrosRepository.getById(dto.getId());
		
		if (!livro.pertenceAoUsuario(logado)) {
			lancarErroAcessoNegado();
		}
		
		livro.atualizarInformacoes(dto.getTitulo(),dto.getDataLancamento(),dto.getNumeroPaginas());
		return modelMapper.map(livro, CadastroLivrosDto.class);
	}
	
	@Transactional
	public void remover(Long id, Usuario logado) {
		CadastroLivros livro = cadastroLivrosRepository.getById(id);
		
		if (!livro.pertenceAoUsuario(logado)) {
			lancarErroAcessoNegado();
		}
		
		cadastroLivrosRepository.deleteById(id);
	}

	public CadastroLivrosDto detalhar(Long id, Usuario logado) {
		CadastroLivros livro = cadastroLivrosRepository
				.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		if (!livro.pertenceAoUsuario(logado)) {
			lancarErroAcessoNegado();
		}
		return modelMapper.map(livro, CadastroLivrosDto.class);
	}
	
	private void lancarErroAcessoNegado() {
		throw new AccessDeniedException("Acesso Negado!");
	}
	
}
