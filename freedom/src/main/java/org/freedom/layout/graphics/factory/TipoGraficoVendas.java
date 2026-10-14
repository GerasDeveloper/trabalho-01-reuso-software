package org.freedom.layout.graphics.factory;

/**
 * Enum representando os tipos de gráficos de evolução de vendas suportados pelo Freedom ERP.
 */
public enum TipoGraficoVendas {

	BARRAS( "B", "Barras 3D" ),
	LINHAS( "L", "Linha horizontal" );

	private final String codigo;
	private final String descricao;

	TipoGraficoVendas( String codigo, String descricao ) {
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoGraficoVendas fromCodigo( String codigo ) {
		if ( codigo == null ) {
			throw new IllegalArgumentException( "Código do tipo de gráfico não pode ser nulo." );
		}
		for ( TipoGraficoVendas tipo : values() ) {
			if ( tipo.codigo.equalsIgnoreCase( codigo.trim() ) ) {
				return tipo;
			}
		}
		throw new IllegalArgumentException( "Tipo de gráfico de vendas desconhecido: " + codigo );
	}
}
