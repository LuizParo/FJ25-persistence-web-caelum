package br.com.caelum.financas.mb;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.CategoriaDao;
import br.com.caelum.financas.dao.ContaDao;
import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.modelo.Categoria;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;

@Named
@ViewScoped
public class MovimentacoesBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MovimentacaoDao movimentacaoDao;
	
	@Inject
	private ContaDao contaDao;
	
	@Inject
	private CategoriaDao categoriaDao;
	
	private List<Movimentacao> movimentacoes;
	private List<Categoria> categorias;
	private Movimentacao movimentacao = new Movimentacao();
	private Integer contaId;
	private Integer categoriaId;
	
	
	public void grava() {
		System.out.println("Fazendo a gravacao da movimentacao");
		this.movimentacao.setConta(this.contaDao.busca(this.contaId));
		
		if(this.movimentacao.getId() == null) {
			this.movimentacaoDao.adiciona(this.movimentacao);
		} else {
			this.movimentacaoDao.altera(this.movimentacao);
		}
		this.movimentacoes = this.movimentacaoDao.listaComCategorias();
		
		limpaFormularioDoJSF();
	}
	
	public void adicionaCategora() {
		if(this.categoriaId != null && this.categoriaId > 0) {
			Categoria categoria = categoriaDao.procura(this.categoriaId);
			this.movimentacao.adicionaCategora(categoria);
		}
	}

	public void remove() {
		System.out.println("Removendo a movimentacao");
		this.movimentacaoDao.remove(this.movimentacao);
		this.movimentacoes = this.movimentacaoDao.listaComCategorias();
		limpaFormularioDoJSF();
	}

	public List<Movimentacao> getMovimentacoes() {
		if(movimentacoes == null) {
			movimentacoes = this.movimentacaoDao.listaComCategorias();
		}
		return movimentacoes;
	}
	
	public List<Categoria> getCategorias() {
		if(this.categorias == null) {
			System.out.println("Listando as categorias");
			this.categorias = this.categoriaDao.lista();
		}
		return this.categorias;
	}
	
	public Movimentacao getMovimentacao() {
		if(movimentacao.getData()==null) {
			movimentacao.setData(Calendar.getInstance());
		}
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Integer getContaId() {
		return contaId;
	}

	public void setContaId(Integer contaId) {
		this.contaId = contaId;
	}
	

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}

	/**
	 * Esse metodo apenas limpa o formulario da forma com que o JSF espera.
	 * Invoque-o no momento manager que precisar do formulario vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.movimentacao = new Movimentacao();
	}

	public TipoMovimentacao[] getTiposDeMovimentacao() {
		return TipoMovimentacao.values();
	}
}
