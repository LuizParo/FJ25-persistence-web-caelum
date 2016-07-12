package br.com.caelum.financas.filters;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.TipoMovimentacao;

public class FiltroMovimentacao {
	private Conta conta = new Conta();
	private TipoMovimentacao tipoMovimentacao;
	private Integer mes;

	public Conta getConta() {
		if(conta.getId()!=null && conta.getId()==0) {
			conta.setId(null);
		}
		return conta;
	}
	
	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public TipoMovimentacao getTipoMovimentacao() {
		return tipoMovimentacao;
	}
	
	public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Integer getMes() {
		if(mes!=null && mes==0) {
			mes=null;
		}
		return mes;
	}
	
	public void setMes(Integer mes) {
		this.mes = mes;
	}
}