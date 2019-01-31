package com.projeto.pi2018.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.projeto.pi2018.azure.model.Candidatos;
import com.projeto.pi2018.model.Pessoa;
import com.projeto.pi2018.service.AzureService;
import com.projeto.pi2018.service.PessoaService;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {
	
	
	@Autowired
	private PessoaService ps;
	@Autowired
	private AzureService as;
	

	/*O método detectFace() da classe controller chama os métodos da service para fazer o reconhecimento de face e
	 *  caso seja cadastrado ele apenas usa o addFace() e o treinar
	 *  caso não seja cadastrado ele chama o método criarPerson(), cria a pessoa no banco e utiliza recursão
	*/
	@PostMapping("/detectFace")
	public @ResponseBody boolean detectFace(@RequestBody Pessoa pessoa) throws Exception {
		String faceId = as.reconhecerFace(pessoa.getFotoTemp()); 
		if(faceId != "Erro") {
			Pessoa p = ps.buscarCpf(pessoa.getCpf());
			if(p != null) {
				as.addFace(p.getPersonId(), pessoa.getFotoTemp());
				as.treinar();
				return true;
			}
			else {
				String personId = as.criarPerson(pessoa.getNome());
				pessoa.setPersonId(personId);
				ps.inserir(pessoa);
				detectFace(pessoa);
			}
		}
		return false;
	}
	/*O método identify() da classe controller chama os métodos da service para fazer o identificar a face 
	 *  caso aquela pessoa já tenha sido cadastrada
	*/
	@PostMapping("/identify")
	public @ResponseBody List<Pessoa> identify(@RequestBody String base64) throws Exception {
		String faceId = as.reconhecerFace(base64);
		if(faceId!="Erro") {
			List<Pessoa> pessoas = new ArrayList<>();
			Pessoa pessoa = new Pessoa();
			for(Candidatos c : as.identify(faceId)){
				pessoa = ps.buscarPersonId(c.getPersonId());
				pessoa.setConfidence(c.getConfidence());
				
				pessoas.add(pessoa);
			}
			
			return pessoas;
		}
		
		return null;
	}
}
