package br.com.triadworks.lanceunico.service;

import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import static org.mockito.Mockito.*;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import br.com.triadworks.lanceunico.dao.PromocaoDao;
import br.com.triadworks.lanceunico.modelo.Promocao;
import br.com.triadworks.lanceunico.teste.builder.CriadorPromocao;
import br.com.triadworks.lanceunico.util.DateUtils;

public class EncerradorDePromocoesTest {

	@Test
	public void deveEncerrarPromocaoForaDaVigencia() {
		
		//cenario
		Date antigaData = DateUtils.novaData("01/05/2017");
		
		Promocao tv = new CriadorPromocao()
								.para("tv")
								.naData(antigaData)
								.cria();
		Promocao xbox = new CriadorPromocao()
								.para("xbox")
								.naData(antigaData)
								.cria();
		
		List<Promocao> promocoes = Arrays.asList(xbox,tv);
		PromocaoDao daoFalso = mock(PromocaoDao.class);
		when(daoFalso.abertas()).thenReturn(promocoes);
		
		//acao
		EncerradorDePromocoes encerra = new EncerradorDePromocoes(daoFalso);
		int total = encerra.encerra();
		
		//validacao
		
		assertEquals("Validar total.",2, total);
		assertTrue("promocao xbox encerrada", xbox.isEncerrada());
		assertTrue("prmocao tv encerrada", tv.isEncerrada());
		
	}
	
	@Test
	public void deveAtualizarPromocoesEncerradas(){
		Date data = new DateUtils().novaData("12/05/2017");
		
		Promocao ipad = new CriadorPromocao()
								.para("Ipda")
								.naData(data)
								.cria();
		
		List<Promocao> promocoes = Arrays.asList(ipad);
		PromocaoDao daoFalso = mock(PromocaoDao.class);
		when(daoFalso.abertas()).thenReturn(promocoes);
		
		EncerradorDePromocoes encerrador = new EncerradorDePromocoes(daoFalso);
		int total = encerrador.encerra();
		
		verify(daoFalso).atualiza(ipad);
	}
	
	@Test
	public void deveEncerrarAsPromocoesRestantesMesmoEmCasoDeFalhas(){
		Date data = new DateUtils().novaData("12/05/2017");
		
		Promocao promo1 = new CriadorPromocao()
								.para("Ipda")
								.naData(data)
								.cria();
		Promocao promo2 = new CriadorPromocao()
								.para("Ipda")
								.naData(data)
								.cria();
		
		List<Promocao> promocoes = Arrays.asList(promo1, promo2);
		PromocaoDao daoFalso = mock(PromocaoDao.class);
		when(daoFalso.abertas()).thenReturn(promocoes);
		
		doThrow(new RuntimeException("Erro lançado, corretamente.")).when(daoFalso).atualiza(promo1);
		
		EncerradorDePromocoes encerrador = new EncerradorDePromocoes(daoFalso);
		int total = encerrador.encerra();
		
		verify(daoFalso).atualiza(promo2);
		assertEquals("total", 1, total);
	}
	
	@Test
	public void deveExecutarPromocoesZeradas(){
		Date data = new DateUtils().novaData("12/05/2017");
		
		Promocao promo1 = new CriadorPromocao()
								.para("Ipda")
								.naData(data)
								.cria();
		Promocao promo2 = new CriadorPromocao()
								.para("Ipda")
								.naData(data)
								.cria();
		
		List<Promocao> promocoes = Arrays.asList(promo1, promo2);
		PromocaoDao daoFalso = mock(PromocaoDao.class);
		when(daoFalso.abertas()).thenReturn(promocoes);
		
		doThrow(new RuntimeException("Erro lançado, corretamente.")).when(daoFalso).atualiza(promo1);
		doThrow(new RuntimeException("Erro lançado, corretamente.")).when(daoFalso).atualiza(promo2);
		
		EncerradorDePromocoes encerrador = new EncerradorDePromocoes(daoFalso);
		int total = encerrador.encerra();
		
		verify(daoFalso).atualiza(promo2);
		assertEquals("total", 0, total);
	}

}


















