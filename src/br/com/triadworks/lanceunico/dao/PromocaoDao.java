package br.com.triadworks.lanceunico.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.triadworks.lanceunico.modelo.Cliente;
import br.com.triadworks.lanceunico.modelo.Lance;
import br.com.triadworks.lanceunico.modelo.Promocao;
import br.com.triadworks.lanceunico.modelo.Status;
import br.com.triadworks.lanceunico.util.JPAUtil;

public class PromocaoDao {
	
	private EntityManager entityManager;
	
	/**
	 * Não utilizado pelo CDI
	 */
	public PromocaoDao() {
		this(new JPAUtil().getEntityManager());
	}
	
	@Inject
	public PromocaoDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public List<Promocao> lista() {
		List<Promocao> promocoes = entityManager
				.createQuery("select p from Promocao p", Promocao.class)
				.getResultList();
		
		return promocoes;
	}
	
	public List<Promocao> abertas() {
		List<Promocao> promocoes = entityManager
				.createQuery("select p from Promocao p "
						+ "where p.status = :status", Promocao.class)
				.setParameter("status", Status.ABERTA)
				.getResultList();
		
		return promocoes;
	}
	
	/**
	 * Retorna as promoções na qual o <code>cliente</code> deu algum lance em um
	 * determinado período.
	 */
	public List<Promocao> abertasPara(Cliente cliente, Date desdeAData) {
		
		List<Promocao> promocoes = entityManager
				.createQuery("select p from Promocao p join p.lances as lance"
						+ "	where p.status = :status "
						+ "	  and lance.cliente = :cliente "
						+ "	  and p.data >= :inicio "
						+ "	order by p.data desc", Promocao.class)
				.setParameter("status", Status.ABERTA)
				.setParameter("cliente", cliente)
				.setParameter("inicio", desdeAData)
				.getResultList();
		
		return promocoes;
	}
	
	/**
	 * Retorna o total de promocões encerradas
	 */
	public Long totalDeEncerradas() {
		Long total = 0L;
		
		List<Promocao> todas = entityManager
				.createQuery("select p from Promocao p", Promocao.class)
				.getResultList();
		
		for (Promocao p : todas) {
			if (p.isEncerrada()) {
				total++;
			}
		}

		return total;
	}
	
	/**
	 * Retorna o total de promocões abertas
	 */
	public Long totalDeAbertas() {
		Long total = 0L;
		
		List<Promocao> todas = entityManager
				.createQuery("select p from Promocao p", Promocao.class)
				.getResultList();
		
		for (Promocao p : todas) {
			if (p.isAberta()) {
				total++;
			}
		}
		
		return total;
	}

	public Promocao carrega(Integer id) {
		return entityManager.find(Promocao.class, id);
	}

	public void salva(Promocao promocao) {
		entityManager.persist(promocao);		
	}
	
	public void atualiza(Promocao promocao) {
		entityManager.merge(promocao);
	}
	
	public void remove(Promocao promocao) {
		entityManager.remove(promocao);
	}
	
	/**
	 * Registra novo lance numa promoção existente
	 */
	public void registraLance(Integer id, Lance lance) {
		
		// procura promocao por id
		Promocao promocao = null;
		List<Promocao> listaPromocoes = lista();
		for (Promocao p : listaPromocoes) {
			if (p.getId().equals(id)) {
				promocao = p;
			}
		}
		
		System.out.println("achou promocao nome=" + promocao.getNome());

		// recarrega informacoes do cliente
		lance.setCliente(entityManager.find(Cliente.class, lance.getCliente().getId()));
		// insere novo lance no banco de dados
		entityManager.persist(lance);
		
		System.out.println("gravou lance no banco id=" + lance.getId());
		
		// cria nova lista com todos os lance da promocao 
		// e adiciona novo lance
		List<Lance> lances = new ArrayList<Lance>();
		lances.addAll(promocao.getLances());
//		lances.add(lance);
		
		promocao.getLances().clear(); // limpa lista (evita erro do hibernate)
		promocao.getLances().addAll(lances); // atualiza lista de lances da promocao
		
//		promocao.getLances().add(lance); // adiciona novo lance
		promocao.registra(lance); // ATENCAO: mandaram usar esse novo metodo
		
		System.out.println("atualiza promocao no banco");
		
		entityManager.merge(promocao); // atualiza promocao no banco
	}

}
