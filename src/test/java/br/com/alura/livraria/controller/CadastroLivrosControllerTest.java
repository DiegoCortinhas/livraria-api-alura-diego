package br.com.alura.livraria.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import br.com.alura.livraria.infra.security.TokenService;
import br.com.alura.livraria.modelo.Perfil;
import br.com.alura.livraria.modelo.Usuario;
import br.com.alura.livraria.repository.PerfilRepository;
import br.com.alura.livraria.repository.UsuarioRepository;
import io.jsonwebtoken.Header;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CadastroLivrosControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private String token;

	private Long idUsuarioLogado;

	@BeforeEach
	public void gerarToken() {
		Usuario logado = new Usuario("Rafaela","rafa@email.com.br","123456");
		Perfil admin = perfilRepository.findById(1l).get();
		logado.adicionarPerfil(admin);
		usuarioRepository.save(logado);
		Authentication authentication = new UsernamePasswordAuthenticationToken(logado, logado.getLogin());
		this.token = tokenService.gerarToken(authentication);
		this.idUsuarioLogado = logado.getId();
	}

	@Test
	void naoDeveriaCadastrarLivroComDadosIncompletos() throws Exception {
		String json = "{}";
		mvc
		.perform(post("/livros")
		.contentType(MediaType.APPLICATION_JSON)
		.content(json)
		.header("Authorization", "Bearer " +token))
		.andExpect(status().isBadRequest());
	}

	@Test
	void deveriaCadastrarLivroComDadosCompletos() throws Exception {
		String jsonAutor = "{\"nome\":\"Diego Cortinhas\",\"email\":\"diegoc@email.com\",\"dataNascimento\":\"1988-03-07\",\"miniCurriculo\":\"Escritor\"}";

		MvcResult mvcAutor = mvc
				.perform(post("/autores")
						.contentType(MediaType.APPLICATION_JSON)
						.content(jsonAutor)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(content().json(jsonAutor)).andReturn();

		Integer idAutor = JsonPath.read(mvcAutor.getResponse().getContentAsString(),"$.id");
		String jsonLivro = "{\"titulo\":\"A culpa ?? das Estrelas\",\"dataLancamento\":\"2004-03-12\",\"numeroPaginas\":500,\"cadastro_autor_id\":"+idAutor+",\"usuario_id\":" +idUsuarioLogado +"}";
		String jsonLivroComAutor = "{\"titulo\":\"A culpa ?? das Estrelas\",\"dataLancamento\":\"2004-03-12\",\"numeroPaginas\":500, \"autor\":" + jsonAutor +"}";

		mvc.perform(
				post("/livros")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonLivro)
				.header("Authorization", "Bearer " + token))
		.andExpect(status().isCreated())
		.andExpect(header().exists("Location"))
		.andExpect(content().json(jsonLivroComAutor));
	}

}