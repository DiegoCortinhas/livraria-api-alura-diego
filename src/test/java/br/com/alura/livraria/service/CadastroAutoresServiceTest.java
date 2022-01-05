package br.com.alura.livraria.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alura.livraria.dto.CadastroAutoresDto;
import br.com.alura.livraria.dto.CadastroAutoresFormDto;
import br.com.alura.livraria.repository.CadastroAutoresRepository;


@ExtendWith(MockitoExtension.class)
class CadastroAutoresServiceTest {
	
	@Mock
	private CadastroAutoresRepository cadastroAutoresRepository;
	
	@InjectMocks
	private CadastroAutoresService service;
	
	@Test
	void deveriaCadastrarUmAutor() {
		CadastroAutoresFormDto formDto = new CadastroAutoresFormDto(
				"Andre da Silva", 
				"andre@email.com", 
				LocalDate.of(1988, 03, 11), 
				"Escritor");
		
		CadastroAutoresDto dto = service.cadastrar(formDto);
		Mockito.verify(cadastroAutoresRepository).save(Mockito.any());
		
		assertEquals(formDto.getNome(), dto.getNome());
		assertEquals(formDto.getEmail(), dto.getEmail());
		assertEquals(formDto.getDataNascimento(), dto.getDataNascimento());
		assertEquals(formDto.getMiniCurriculo(), dto.getMiniCurriculo());
		
	}

}
