package br.com.triadworks.lanceunico.modelo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.triadworks.lanceunico.service.sorteio;
import br.com.triadworks.lanceunico.teste.builder.CriadorPromocao;

public class PromocaoTest {

	private sorteio sorteio;
	private Cliente rafael;
	private Cliente rommel;
	private Cliente handerson;
	
	@Before
	public void setUp(){
		this.sorteio = new sorteio();
		this.rafael = new Cliente("Rafael");
		this.rommel = new Cliente("Rommel");
		this.handerson = new Cliente("Handerson");
	}
	
	@Test
	public void deveRegistrarUmLance(){
		
		Promocao promocao = new CriadorPromocao()
									.para("playStation")
									.comLance(rafael, 20.20)
									.cria();
		List<Lance> lances = promocao.getLances();
		
		assertEquals(1, lances.size());
		assertEquals(20.20, lances.get(0).getValor(), 0.0001);
		assertEquals(rafael, lances.get(0).getCliente());
	}
	
	@Test
	public void deveRegistrarVarioslances(){
		
		Promocao promocao = new CriadorPromocao()
									.para("playStation")
									.comLance(rafael, 20.20)
									.comLance(handerson, 30.10)
									.cria();
		List<Lance> lances = promocao.getLances();
		
		assertEquals(2, lances.size());
		assertEquals(20.20, lances.get(0).getValor(), 0.0001);
		assertEquals(rafael, lances.get(0).getCliente());
		assertEquals(30.10, lances.get(1).getValor(), 0.0001);
		assertEquals(handerson, lances.get(1).getCliente());
		
	}
	
	@Test
	public void naoDeveRegistrarDoislancesSeguidosDoMesmoCliente(){
		
		Promocao promocao = new CriadorPromocao()
									.para("ps4")
									.comLance(rafael, 20.20)
									.comLance(rafael, 30.10)
									.cria();
		List<Lance> lances = promocao.getLances();
		
		assertEquals(1, lances.size());
		assertEquals(20.20, lances.get(0).getValor(), 0.001);
		
	}
	
	@Test(expected = RuntimeException.class)
	public void naoDeveRegistrarLancesComValorNegativo(){
		
		Promocao promocao = new CriadorPromocao()
									.para("ps3")
									.comLance(rafael, -10.00)
									.cria();
	}

}
















