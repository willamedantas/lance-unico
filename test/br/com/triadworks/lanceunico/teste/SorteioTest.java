package br.com.triadworks.lanceunico.teste;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.*;

import br.com.triadworks.lanceunico.modelo.Cliente;
import br.com.triadworks.lanceunico.modelo.Lance;
import br.com.triadworks.lanceunico.modelo.Promocao;
import br.com.triadworks.lanceunico.service.sorteio;
import br.com.triadworks.lanceunico.teste.builder.CriadorPromocao;

public class SorteioTest {
	
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
	
/*	@After
	public void finaliza(){
		System.out.println("@After");
	}
	
	@Test
	public void teste1(){
		System.out.println("Test - 1");
	}*/
	
	@Test
	public void deveSortearlancesOrdemCrescente() {
		
		Promocao promocao = new CriadorPromocao()
									.para("xbox")
									.comLance(rafael,250)
									.comLance(rommel,300)
									.comLance(handerson,400)
									.cria();
		
		sorteio.sortear(promocao);
		
		double maiorEsperado = 400;
		double menorEsperado = 250;
		
		Assert.assertEquals(maiorEsperado, sorteio.getMaiorLance(), 0.0001);
		Assert.assertEquals(menorEsperado, sorteio.getMenorLance(),0.0001);
	}
	
	@Test
	public void deveSortearLancesOrdemDecrescente() {
		
		Promocao promocao = new CriadorPromocao()
								.para("xbox")
								.comLance(rafael,400)
								.comLance(rommel,300)
								.comLance(handerson,250)
								.cria();
		
		sorteio.sortear(promocao);
		
		double maiorEsperado = 400;
		double menorEsperado = 250;
		
		assertEquals(maiorEsperado, sorteio.getMaiorLance(), 0.0001);
		assertEquals(menorEsperado, sorteio.getMenorLance(),0.0001);
	}
	
	@Test
	public void deveSortearQuandoHouverApenasUmLance() {
		
		Promocao promocao = new CriadorPromocao()
								.para("xbox")
								.comLance(rafael,600.0)
								.cria();
		
		sorteio.sortear(promocao);
		
		double maiorEsperado = 600.0;
		double menorEsperado = 600.0;
		
		Assert.assertEquals(maiorEsperado, sorteio.getMaiorLance(), 0.0001);
		Assert.assertEquals(menorEsperado, sorteio.getMenorLance(),0.0001);
	}
	
	
	@Test
	public void deveSortearLancesEmOrdemAleatoria() {
		
		Promocao promocao = new CriadorPromocao()
									.para("xbox")
									.comLance(rafael,1050)
									.comLance(rommel,2990.99)
									.comLance(handerson,24.70)
									.comLance(rafael,477.00)
									.comLance(handerson,1.25)
									.cria();
		
		sorteio.sortear(promocao);
		
		double maiorEsperado = 2990.99;
		double menorEsperado = 1.25;
		
		Assert.assertEquals(maiorEsperado, sorteio.getMaiorLance(), 0.0001);
		Assert.assertEquals(menorEsperado, sorteio.getMenorLance(),0.0001);
	}
	
	@Test
	public void deveSortearOsTresMenoresLances() {
		
		Promocao promocao = new CriadorPromocao()
									.para("xbox")
									.comLance(rafael,300.0)
									.comLance(rommel,100.00)
									.comLance(handerson,20.00)
									.comLance(rafael,440.00)
									.comLance(handerson,1.25)
									.cria();
		
		sorteio sorteio = new sorteio();
		sorteio.sortear(promocao);
		
		List<Lance> menores = sorteio.getTresMenores();
		
		Assert.assertEquals(3, menores.size(), 0.0001);
		Assert.assertEquals(1.25, menores.get(0).getValor(),0.0001);
		Assert.assertEquals(20.00, menores.get(1).getValor(),0.0001);
		Assert.assertEquals(100.00, menores.get(2).getValor(),0.0001);
	}
	
	@Test
	public void deveSortearTodosLancesQuandoHouverMenos3Lances() {
		
		Promocao promocao = new CriadorPromocao()
									.para("xbox")
									.comLance(rafael,600.0)
									.comLance(rommel,400.0)
									.cria();
		
		sorteio.sortear(promocao);
		
		List<Lance> menores = sorteio.getTresMenores();
		
		Assert.assertEquals(2, menores.size(), 0.0001);
		Assert.assertEquals(400.0, menores.get(0).getValor(),0.0001);
		Assert.assertEquals(600.0, menores.get(1).getValor(),0.0001);
		
	}
	
	@Test
	public void naoDeveSortearQuandoNaoHouverLances() {
		
		Promocao promocao = new Promocao("xbox");
		
		sorteio sorteio = new sorteio();
		sorteio.sortear(promocao);
		
		List<Lance> menores = sorteio.getTresMenores();
		
		Assert.assertEquals(null, menores.get(0), 0.0001);

	}
	

}
