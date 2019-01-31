package com.projeto.pi2018.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.pi2018.azure.Azure;
import com.projeto.pi2018.azure.model.Candidatos;

@Service
public class AzureService {

	private Azure azure = new Azure();
	
	public String reconhecerFace(String fotoTemp) throws Exception {
		return azure.reconhecerFace(fotoTemp);
	}
	
	public String criarPerson(String nome) throws Exception{
		return azure.criarPerson(nome);
	}
	public void treinar() throws Exception {
		azure.train();
	}
	
	public void addFace(String personId,String faceId)throws Exception {
		azure.addFace(personId, faceId);
	}
	
	public List<Candidatos> identify(String faceId) throws Exception {
		return azure.identify(faceId);
	}
}
