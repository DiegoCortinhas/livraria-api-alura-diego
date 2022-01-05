package br.com.alura.livraria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.livraria.dto.ItemLivrariaDto;
import br.com.alura.livraria.repository.CadastroLivrosRepository;

@Service
public class RelatorioService {
	
	@Autowired
	private CadastroLivrosRepository repository;
	
	public List<ItemLivrariaDto> relatorioQuantidadeLivros() {
		
		return repository.relatorioQuantidadeLivros();
	}

}
