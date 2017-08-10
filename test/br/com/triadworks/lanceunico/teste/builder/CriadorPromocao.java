package br.com.triadworks.lanceunico.teste.builder;

import java.util.Date;

import br.com.triadworks.lanceunico.modelo.Cliente;
import br.com.triadworks.lanceunico.modelo.Lance;
import br.com.triadworks.lanceunico.modelo.Promocao;

public class CriadorPromocao {
	
	private Promocao promocao;
	
	public CriadorPromocao naData(Date data){
		promocao.setData(data);
		return this;
	}
	
	public CriadorPromocao para(String nome){
		promocao = new Promocao(nome);
		return this;
	}
	
	public CriadorPromocao comLance(Cliente cliente, double valor){
		promocao.registra(new Lance(cliente,valor));
		return this;
	}
	
	public Promocao cria(){
		return promocao;
	}

}
