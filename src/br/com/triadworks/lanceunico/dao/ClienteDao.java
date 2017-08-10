package br.com.triadworks.lanceunico.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.triadworks.lanceunico.modelo.Cliente;

public class ClienteDao {
	
	private final EntityManager entityManager;
	
	@Inject
	public ClienteDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Cliente buscaPorEmail(String email) {
		
		String jpql = "select c from Cliente c where x.email = :email";
		
		Cliente cliente = (Cliente) entityManager
				.createQuery(jpql)
				.setParameter("email", email)
				.getSingleResult();
		
		return cliente;
	}

	public List<Cliente> lista() {
		List<Cliente> clientes = entityManager
				.createQuery("select c from Cliente c", Cliente.class)
				.getResultList();
		
		return clientes;
	}

	public void salva(Cliente cliente) {
		entityManager.persist(cliente);
	}

	public void remove(Cliente cliente) {
		cliente = carrega(cliente.getId());
		entityManager.remove(cliente);
	}

	public Cliente carrega(Integer id) {
		return entityManager.find(Cliente.class, id); 
	}

	public void atualiza(Cliente cliente) {
		entityManager.merge(cliente);
	}

}
