package br.com.caelum.financas.modelo;

import java.math.BigDecimal;

public class ValorPorMesEAno {
	private BigDecimal valor;
	private int mes;
	private int ano;

	public ValorPorMesEAno(BigDecimal valor, int mes, int ano) {
		this.valor = valor;
		this.mes = mes;
		this.ano = ano;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public int getMes() {
		return mes;
	}

	public int getAno() {
		return ano;
	}
}