package br.com.caelum.financas.mb;

import java.util.List;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import br.com.caelum.financas.modelo.Movimentacao;

@Named
@RequestScoped
public class TestaNMaisUmBean {
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public String vai() {
		List<Movimentacao> movs = em.createQuery("from Movimentacao m left join fetch m.categorias", Movimentacao.class)
				.getResultList();
		for (Movimentacao mov : movs) {
			System.out.println(mov.getCategorias().size());
		}
		return "nmaisum?faces-redirect=true";
	}
}