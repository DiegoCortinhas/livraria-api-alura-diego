package br.com.alura.livraria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Null;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import br.com.alura.livraria.dto.CadastroAutoresDto;
import br.com.alura.livraria.dto.CadastroAutoresFormDto;
import br.com.alura.livraria.dto.CadastroLivrosDto;
import br.com.alura.livraria.dto.CadastroLivrosFormDto;
import br.com.alura.livraria.modelo.CadastroAutores;
import br.com.alura.livraria.modelo.CadastroLivros;
import br.com.alura.livraria.modelo.Usuario;
import br.com.alura.livraria.repository.CadastroAutoresRepository;
import br.com.alura.livraria.repository.CadastroLivrosRepository;
import br.com.alura.livraria.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CadastroLivrosServiceTest {
	
	@Mock
	private CadastroLivrosRepository cadastroLivrosRepository;
	@Mock
	private CadastroAutoresRepository cadastroAutoresRepository;
	@Mock
	private UsuarioRepository usuarioRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private CadastroLivrosService service;
	
	private Usuario logado;
	
	@BeforeEach
	public void before() {
		this.logado = new Usuario("Rafaela","rafa@email.com.br","123456");
		
	}
		
	private CadastroLivrosFormDto criarLivrosFormDto(String titulo, Integer quantidade, Long id) {
		CadastroLivrosFormDto formDto = new CadastroLivrosFormDto(
				titulo, 
				LocalDate.now(), 
				quantidade, 
				id,
				1L);
		return formDto;
	}
	
	private CadastroAutoresFormDto criarAutorFormDto(String nome, String email, String miniCurriculo) {
		CadastroAutoresFormDto formDto = new CadastroAutoresFormDto(
				nome, 
				email, 
				null, 
				miniCurriculo);
		
		return formDto;
	}
	
	@Test
	void deveriaCadastrarUmLivro() {
		
		CadastroLivrosFormDto formDto = criarLivrosFormDto("A culpa é das Estrelas",570,2l);
		CadastroAutoresFormDto formAutorDto = criarAutorFormDto("Diego","diegoc@email.com","Escritor");
		CadastroAutores cadastro = modelMapper.map(formAutorDto,CadastroAutores.class);
		CadastroAutoresDto cadastroAutoresDto = modelMapper.map(cadastro,CadastroAutoresDto.class);
		//cadastro.setId(null);
		
		Mockito
		.when(usuarioRepository.getById(formDto.getUsuarioId()))
		.thenReturn(logado);
		
		CadastroLivros livro = new CadastroLivros(
				formDto.getTitulo(), 
				formDto.getDataLancamento(), 
				formDto.getNumeroPaginas(), 
				cadastro, 
				logado);
		
		Mockito
		.when(modelMapper.map(formDto, CadastroLivros.class))
		.thenReturn(livro);
		
		
		Mockito
		.when(modelMapper.map(livro, CadastroLivrosDto.class))
		.thenReturn(new CadastroLivrosDto(
				null, 
				livro.getTitulo(), 
				livro.getDataLancamento(), 
				livro.getNumeroPaginas(), 
				cadastroAutoresDto));
		
				
		CadastroLivrosDto dto = service.cadastrar(formDto,logado);
		
		Mockito.verify(cadastroLivrosRepository).save(Mockito.any());
		
		assertEquals(formDto.getTitulo(), dto.getTitulo());
		assertEquals(formDto.getNumeroPaginas(), dto.getNumeroPaginas());
		assertEquals(formDto.getDataLancamento(), dto.getDataLancamento());
	}

			
	@Test
	void naoDeveriaCadastrarUmLivroComAutorInexistente() {
		
		CadastroLivrosFormDto formDto = criarLivrosFormDto("A culpa é das Estrelas",570,999999l);
		
		Mockito
		.when(cadastroAutoresRepository.getById(formDto.getCadastroAutorId()))
		.thenThrow(EntityNotFoundException.class);
		
		assertThrows(IllegalArgumentException.class,() -> service.cadastrar(formDto,logado));
		
	}

}
