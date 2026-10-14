package org.freedom.layout.graphics.adapter;

import org.freedom.library.functions.Funcoes;

/**
 * Encapsula os dados de venda por período e valor,
 * fornecendo a formatação padronizada utilizada pelos gráficos do Freedom ERP.
 */
public class RegistroVenda {

	private final int mes;
	private final String ano;
	private final double valor;

	public RegistroVenda(int mes, String ano, double valor) {
		this.mes = mes;
		this.ano = ano != null ? ano.trim() : "";
		this.valor = valor;
	}

	public int getMes() {
		return mes;
	}

	public String getAno() {
		return ano;
	}

	public double getValor() {
		return valor;
	}

	/**
	 * Retorna a legenda/rótulo formatado no padrão legado:
	 * ex: "Jan/2026 >       1.500,00 ) "
	 */
	public String getRotuloFormatado() {
		return Funcoes.adicionaEspacos( Funcoes.strMes( mes ), 3 ) 
				+ "/" + ano 
				+ " >" + Funcoes.strDecimalToStrCurrency( 14, 2, String.valueOf( valor ) ) 
				+ " ) ";
	}
}
