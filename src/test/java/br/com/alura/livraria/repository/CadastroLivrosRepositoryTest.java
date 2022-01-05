package br.com.alura.livraria.repository;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.alura.livraria.dto.ItemLivrariaDto;
import br.com.alura.livraria.modelo.CadastroAutores;
import br.com.alura.livraria.modelo.CadastroLivros;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class CadastroLivrosRepositoryTest {
	
	@Autowired
	private CadastroLivrosRepository repository;
	
	@Autowired
	private TestEntityManager em;
	
	@Test
	void deveriaRetornarRelatorioDeQuantidadeDeLivros() {
		
		CadastroAutores a1 = new CadastroAutores(
				"Andre da Silva", 
				"andre@email.com", 
				LocalDate.of(1988, 03, 11), 
				"Escritor");
		em.persist(a1);
		
		CadastroAutores a2 = new CadastroAutores(
				"Fernanda Nogueira", 
				"fernanda.nogueira@email.com", 
				LocalDate.of(1992, 05, 11), 
				"Escritora");
		em.persist(a2);
		
		CadastroAutores a3 = new CadastroAutores(
				"Juliana Carvalho", 
				"juliana.carvalho@email.com", 
				LocalDate.of(1983, 07, 13), 
				"Escritora");
		em.persist(a3);
		
		CadastroLivros c1 = new CadastroLivros(
				"Aprenda Java em 21 dias",
				LocalDate.of(2004, 03, 12),
				500,
				a1, 
				null);
		em.persist(c1);
		
		CadastroLivros c2 = new CadastroLivros(
				"Como ser mais produtivo",
				LocalDate.of(2004, 04, 21),
				500,
				a2,
				null);
		em.persist(c2);
		
		CadastroLivros c3 = new CadastroLivros(
				"Aprenda a falar em publico",
				LocalDate.of(2004, 07, 01),
				400,
				a3,
				null);
		em.persist(c3);
		
		CadastroLivros c4 = new CadastroLivros(
				"Otimizando o seu tempo",
				LocalDate.of(2004, 12, 10),
				400,
				a2,
				null);
		em.persist(c4);
		
		List <ItemLivrariaDto> relatorio =repository.relatorioQuantidadeLivros();
		
		//assertEquals(3, relatorio.size());
		Assertions.assertThat(relatorio)
		.hasSize(3)
		.extracting(ItemLivrariaDto::getAutor, ItemLivrariaDto::getQuantidade, ItemLivrariaDto::getPercentual)
		.containsExactlyInAnyOrder(
				Assertions.tuple("Andre da Silva",1l,0.25),
				Assertions.tuple("Fernanda Nogueira",2l,0.50),
				Assertions.tuple("Juliana Carvalho",1l,0.25)
				);
		
		
	}

}
