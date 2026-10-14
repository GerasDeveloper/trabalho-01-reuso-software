package org.freedom.layout.graphics.factory;

import java.sql.ResultSet;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.layout.graphics.EvoluVendasBarras;
import org.freedom.layout.graphics.EvoluVendasLinha;
import org.freedom.library.component.LeiauteGR;

/**
 * Padrão GoF Factory Method / Static Factory (Criacional).
 * Centraliza e encapsula a instanciação dos relatórios gráficos de evolução de vendas,
 * desacoplando clientes como FREvoluVendas da criação direta de subclasses concretas.
 */
public class RelatorioEvolucaoVendasFactory {

	public static LeiauteGR criarRelatorio( TipoGraficoVendas tipo ) {
		if ( tipo == null ) {
			throw new IllegalArgumentException( "O tipo de gráfico de vendas não pode ser nulo." );
		}

		switch ( tipo ) {
			case BARRAS:
				return new EvoluVendasBarras();
			case LINHAS:
				return new EvoluVendasLinha();
			default:
				throw new IllegalArgumentException( "Tipo de gráfico não reconhecido: " + tipo );
		}
	}

	public static LeiauteGR criarRelatorio( String tipoCodigo ) {
		TipoGraficoVendas tipo = TipoGraficoVendas.fromCodigo( tipoCodigo );
		return criarRelatorio( tipo );
	}

	public static LeiauteGR criarRelatorio( String tipoCodigo, DbConnection con, ResultSet rs ) {
		LeiauteGR relatorio = criarRelatorio( tipoCodigo );
		relatorio.setConexao( con );
		if ( relatorio instanceof EvoluVendasBarras ) {
			( (EvoluVendasBarras) relatorio ).setConsulta( rs );
		} else if ( relatorio instanceof EvoluVendasLinha ) {
			( (EvoluVendasLinha) relatorio ).setConsulta( rs );
		}
		return relatorio;
	}
}
