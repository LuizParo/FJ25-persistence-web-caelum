package br.com.caelum.financas.mb;

import br.com.caelum.financas.modelo.Conta;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named
@RequestScoped
public class CacheBean {

	private Integer id;
	private Conta conta;

	@Inject
	private EntityManager manager;

	public void pesquisar() {
		System.out.println("Testando cache de primeiro nivel");
		this.manager.find(Conta.class, id);
		this.manager.find(Conta.class, id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Conta getConta() {
		return conta;
	}
}