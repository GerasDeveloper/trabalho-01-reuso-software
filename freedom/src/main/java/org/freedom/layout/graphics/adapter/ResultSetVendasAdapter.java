package org.freedom.layout.graphics.adapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador concreto do padrão GoF Adapter.
 * Adapta a interface legada java.sql.ResultSet (Adaptee) para a interface
 * VendasDataAdapter (Target) requerida pelos relatórios.
 */
public class ResultSetVendasAdapter implements VendasDataAdapter {

	private final ResultSet resultSet;

	public ResultSetVendasAdapter(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public List<RegistroVenda> carregarRegistros() throws SQLException {
		if (resultSet == null) {
			return Collections.emptyList();
		}

		List<RegistroVenda> registros = new ArrayList<RegistroVenda>();
		while (resultSet.next()) {
			double valor = resultSet.getDouble(1);
			int mes = resultSet.getInt(2);
			String ano = resultSet.getString(3);
			registros.add(new RegistroVenda(mes, ano, valor));
		}
		return registros;
	}
}
