package br.com.caelum.financas.mb;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class OlaMundoBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String texto;
	private String saida;
	
	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getSaida() {
		return saida;
	}

	public void setSaida(String saida) {
		this.saida = saida;
	}

	public void vai() {
		System.out.println(this.texto);
		this.saida = this.texto;
		this.texto = "";
	}
}