package br.com.triadworks.lanceunico.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.triadworks.lanceunico.modelo.Lance;
import br.com.triadworks.lanceunico.modelo.Promocao;

public class sorteio {
	
	private double maiorDeTodos = Double.NEGATIVE_INFINITY;
	private double menorDeTodos = Double.POSITIVE_INFINITY;
	private List<Lance> menores;
	
	public void sortear(Promocao promocao){
		
		encontraMenorEMaiorLance(promocao);
		
		encontra3MenoresLances();
			
	}

	private void encontra3MenoresLances() {
		Collections.sort(menores, new Comparator<Lance>() {
				public int compare(Lance o1, Lance o2){
					if(o1.getValor() < o2.getValor()) return -1;
					if(o1.getValor() > o2.getValor()) return 1;
					return 0;
				}
		});
			
		int tamanho = menores.size() > 3 ? 3 : menores.size();
		menores = menores.subList(0, tamanho);
	}

	private void encontraMenorEMaiorLance(Promocao promocao) {
		for(Lance lance : promocao.getLances()){
			if(lance.getValor() > maiorDeTodos){
				maiorDeTodos = lance.getValor();
			}
			
			if(lance.getValor() < menorDeTodos){
				menorDeTodos = lance.getValor();
			}
			
			menores = new ArrayList<Lance>(promocao.getLances());
		}
	}
	
	public double getMaiorLance(){
		return this.maiorDeTodos;
	}
	
	public double getMenorLance(){
		return this.menorDeTodos;
	}
	
	public List<Lance> getTresMenores(){
		return this.menores;
	}

}
