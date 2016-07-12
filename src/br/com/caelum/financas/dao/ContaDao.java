package br.com.caelum.financas.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.com.caelum.financas.modelo.Conta;

@Stateless
public class ContaDao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public void adiciona(Conta conta) {
		this.manager.joinTransaction();
		this.manager.persist(conta);
	}
	
	public void altera(Conta conta) {
		this.manager.joinTransaction();
		this.manager.merge(conta);
	}

	public Conta busca(Integer id) {
		return this.manager.find(Conta.class, id);
	}

	public List<Conta> lista() {
		return this.manager.createQuery("select c from Conta c", Conta.class)
				.getResultList();
	}

	public void remove(Conta conta) {
		this.manager.joinTransaction();
		Conta contaParaRemover = this.manager.find(Conta.class, conta.getId());
		this.manager.remove(contaParaRemover);
	}
	
	public int trocaNomeDoBancoEmLote(String antigoNomeBanco, String novoNomeBanco) {
		return this.manager.createQuery("UPDATE Conta c SET c.banco = :novoNome WHERE c.banco = :antigoNome")
				.setParameter("antigoNome", antigoNomeBanco)
				.setParameter("novoNome", novoNomeBanco)
				.executeUpdate();
	}
}