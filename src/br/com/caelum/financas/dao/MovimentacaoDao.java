package br.com.caelum.financas.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.caelum.financas.exception.ValorInvalidoException;
import br.com.caelum.financas.filters.FiltroMovimentacao;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

@Stateless
public class MovimentacaoDao implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Movimentacao m ");
		jpql.append("where m.conta = :conta order by m.valor desc");

		TypedQuery<Movimentacao> query = this.manager.createQuery(jpql.toString() , Movimentacao.class);
		query.setParameter("conta", conta);
		
		return query.getResultList();
	}
	
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Movimentacao m ");
		jpql.append("where m.valor <= :valor and m.tipoMovimentacao = :tipo");
		
		return this.manager
				.createQuery(jpql.toString(), Movimentacao.class)
				.setParameter("valor", valor)
				.setParameter("tipo", tipo)
				.setHint("org.hibernate.cacheable", "true")
				.getResultList();
	}
	
	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select sum(m.valor) from Movimentacao m ");
		jpql.append("where m.conta = :conta and m.tipoMovimentacao = :tipo");
		
		return this.manager
				.createQuery(jpql.toString(), BigDecimal.class)
				.setParameter("conta", conta)
				.setParameter("tipo", tipo)
				.getSingleResult();
	}
	
	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select m from Movimentacao m ");
		jpql.append("where m.conta.titular like :titular");
		
		return this.manager
				.createQuery(jpql.toString(), Movimentacao.class)
				.setParameter("titular", "%" + titular + "%")
				.getResultList();
	}
	
	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo) {
		StringBuilder jpql = new StringBuilder();
		jpql.append("select new " + ValorPorMesEAno.class.getName() + "(sum(m.valor), month(m.data), year(m.data)) ");
		jpql.append("from Movimentacao m ");
		jpql.append("where m.conta = :conta and m.tipoMovimentacao = :tipo ");
		jpql.append("group by year(m.data)||month(m.data) ");
		jpql.append("order by sum(m.valor) desc");
		
		return this.manager
				.createQuery(jpql.toString(), ValorPorMesEAno.class)
				.setParameter("conta", conta)
				.setParameter("tipo", tipo)
				.getResultList();
	}
	
	public List<Movimentacao> listaComCategorias() {
		TypedQuery<Movimentacao> query = this.manager.createQuery("select m from Movimentacao M left join fetch m.categorias", Movimentacao.class);
		return query.getResultList();
	}
	
	public List<Movimentacao> getTodasComCriteria() {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> query = builder.createQuery(Movimentacao.class);
		query.from(Movimentacao.class);
		
		return this.manager.createQuery(query).getResultList();
	}
	
	public BigDecimal somaMovimentacoesDoTitular(String titular) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> query = builder.createQuery(BigDecimal.class);
		Root<Movimentacao> root = query.from(Movimentacao.class);
		
		query.select(
			builder.sum(
				root.<BigDecimal>get("valor")
			)
		);
		
		query.where(builder.like(root.<Conta>get("conta").<String>get("titular"), "%" + titular + "%"));
		
		return this.manager.createQuery(query).getSingleResult();
	}
	
	public List<Movimentacao> pesquisa(FiltroMovimentacao filtro) {
		CriteriaBuilder builder = this.manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> query = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> from = query.from(Movimentacao.class);
		Predicate conjunction = builder.conjunction();
		
		if(filtro.getConta() != null && filtro.getConta().getId() != null) {
			conjunction = builder.and(conjunction, builder.equal(from.<Conta>get("conta"), filtro.getConta()));
		}
		
		if(filtro.getMes() != null) {
			Expression<Integer> expression = builder.function("month", Integer.class, from.<Calendar>get("data"));
			conjunction = builder.and(conjunction, builder.equal(expression, filtro.getMes()));
		}
		
		if(filtro.getTipoMovimentacao() != null) {
			conjunction = builder.and(conjunction, builder.equal(from.<TipoMovimentacao>get("tipoMovimentacao"), filtro.getTipoMovimentacao()));
		}
		
		query.where(conjunction);
		return this.manager.createQuery(query).getResultList();
	}

	public void adiciona(Movimentacao movimentacao) {
		this.manager.joinTransaction();
		this.manager.persist(movimentacao);
		
		if(movimentacao.getValor().compareTo(BigDecimal.TEN) < 0) {
			throw new ValorInvalidoException("Movimentação negativa");
		}
	}

	public Movimentacao busca(Integer id) {
		return this.manager.find(Movimentacao.class, id);
	}

	public List<Movimentacao> lista() {
		return this.manager.createQuery("select m from Movimentacao m", Movimentacao.class).getResultList();
	}

	public void remove(Movimentacao movimentacao) {
		this.manager.joinTransaction();
		Movimentacao movimentacaoParaRemover = this.manager.find(Movimentacao.class, movimentacao.getId());
		this.manager.remove(movimentacaoParaRemover);
	}

	public void altera(Movimentacao movimentacao) {
		this.manager.joinTransaction();
		this.manager.merge(movimentacao);
	}

}
