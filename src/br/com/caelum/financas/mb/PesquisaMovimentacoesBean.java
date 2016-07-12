package br.com.caelum.financas.mb;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.financas.dao.MovimentacaoDao;
import br.com.caelum.financas.filters.FiltroMovimentacao;
import br.com.caelum.financas.modelo.Movimentacao;

@Named
@RequestScoped
public class PesquisaMovimentacoesBean {

	private FiltroMovimentacao filtro = new FiltroMovimentacao();
	private List<Movimentacao> movimentacoes;

	@Inject
	private MovimentacaoDao movimentacaoDao;
	
	public void pesquisa() {
		System.out.println("Pesquisando pelos filtros solicitados");
		this.movimentacoes = this.movimentacaoDao.pesquisa(filtro);
	}
	
	public FiltroMovimentacao getFiltro() {
		return filtro;
	}
	
	public List<Movimentacao> getMovimentacoes() {
		return movimentacoes;
	}
}