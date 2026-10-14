package org.freedom.layout.graphics.adapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adaptador alternativo para dados em memória.
 * Permite alimentar relatórios com dados controlados e isolados,
 * viabilizando a execução de testes automatizados independentes do banco de dados legado.
 */
public class MemoriaVendasAdapter implements VendasDataAdapter {

	private final List<RegistroVenda> registros = new ArrayList<RegistroVenda>();

	public MemoriaVendasAdapter() {
	}

	public MemoriaVendasAdapter(List<RegistroVenda> registrosIniciais) {
		if (registrosIniciais != null) {
			this.registros.addAll(registrosIniciais);
		}
	}

	public MemoriaVendasAdapter adicionarRegistro(int mes, String ano, double valor) {
		this.registros.add(new RegistroVenda(mes, ano, valor));
		return this;
	}

	public List<RegistroVenda> carregarRegistros() throws SQLException {
		return Collections.unmodifiableList(registros);
	}
}
