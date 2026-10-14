package org.freedom.layout.graphics.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.freedom.layout.graphics.EvoluVendasBarras;
import org.freedom.layout.graphics.EvoluVendasLinha;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

/**
 * Testes pós-refatoração (Padrão GoF Adapter):
 * Garante que:
 * 1. O código refatorado mantém o mesmo comportamento legado (comportamento X) quando fornecido um ResultSet.
 * 2. As classes agora dependem estritamente do VendasDataAdapter, lançando erro se não configurado.
 * 3. O Adapter permite alimentar EvoluVendasBarras e EvoluVendasLinha com dados em memória, sem depender de banco de dados.
 * 4. As classes do padrão Adapter (RegistroVenda, ResultSetVendasAdapter, MemoriaVendasAdapter) operam corretamente.
 */
public class VendasAdapterTest {

	private ResultSet criarResultSetMock(final List<Object[]> linhas) {
		InvocationHandler handler = new InvocationHandler() {
			private int index = -1;

			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				String name = method.getName();
				if ("next".equals(name)) {
					index++;
					return index < linhas.size();
				}
				else if ("getDouble".equals(name)) {
					int col = ((Integer) args[0]).intValue();
					return ((Number) linhas.get(index)[col - 1]).doubleValue();
				}
				else if ("getInt".equals(name)) {
					int col = ((Integer) args[0]).intValue();
					return ((Number) linhas.get(index)[col - 1]).intValue();
				}
				else if ("getString".equals(name)) {
					int col = ((Integer) args[0]).intValue();
					Object val = linhas.get(index)[col - 1];
					return val != null ? val.toString() : null;
				}
				return null;
			}
		};

		return (ResultSet) Proxy.newProxyInstance(
			ResultSet.class.getClassLoader(),
			new Class<?>[] { ResultSet.class },
			handler
		);
	}

	/* =========================================================================
	 * VALIDAÇÃO DO MESMO COMPORTAMENTO X: Retrocompatibilidade com ResultSet
	 * ========================================================================= */
	@Test
	public void deveProcessarBarrasComResultSet() throws Exception {
		List<Object[]> linhas = new ArrayList<Object[]>();
		linhas.add(new Object[] { 1500.50, 1, "2026" });
		linhas.add(new Object[] { 2300.00, 2, "2026" });

		ResultSet rs = criarResultSetMock(linhas);

		EvoluVendasBarras barras = new EvoluVendasBarras();
		barras.setConsulta(rs);

		assertNotNull(barras.getDataAdapter());
		assertTrue(barras.getDataAdapter() instanceof ResultSetVendasAdapter);

		barras.processarDados();

		DefaultCategoryDataset dataset = barras.getDatasetBar();
		assertEquals(2, dataset.getRowCount());
		assertEquals(1500.50, ((Number) dataset.getValue(0, 0)).doubleValue(), 0.001);
		assertEquals(2300.00, ((Number) dataset.getValue(1, 0)).doubleValue(), 0.001);
		assertTrue(dataset.getRowKey(0).toString().contains("Jan"));
		assertTrue(dataset.getRowKey(0).toString().contains("2026"));
		assertTrue(dataset.getRowKey(1).toString().contains("Fev"));
	}

	@Test
	public void deveProcessarLinhaComResultSet() throws Exception {
		List<Object[]> linhas = new ArrayList<Object[]>();
		linhas.add(new Object[] { 3100.00, 3, "2026" });
		linhas.add(new Object[] { 4200.00, 4, "2026" });

		ResultSet rs = criarResultSetMock(linhas);

		EvoluVendasLinha linha = new EvoluVendasLinha();
		linha.setConsulta(rs);

		assertNotNull(linha.getDataAdapter());
		assertTrue(linha.getDataAdapter() instanceof ResultSetVendasAdapter);

		linha.processarDados();

		XYSeriesCollection dataset = linha.getDatasetLinha();
		assertEquals(1, dataset.getSeriesCount());
		assertEquals(2, dataset.getSeries(0).getItemCount());
		assertEquals(3.0, dataset.getSeries(0).getX(0).doubleValue(), 0.001);
		assertEquals(3100.00, dataset.getSeries(0).getY(0).doubleValue(), 0.001);
		assertEquals(4.0, dataset.getSeries(0).getX(1).doubleValue(), 0.001);
		assertEquals(4200.00, dataset.getSeries(0).getY(1).doubleValue(), 0.001);

		assertEquals(2, linha.getVLegenda().size());
		assertTrue(linha.getVLegenda().get(0).contains("Mar"));
		assertTrue(linha.getVLegenda().get(1).contains("Abr"));
	}

	/* =========================================================================
	 * VALIDAÇÃO DA DEPENDÊNCIA ESTRITA DO ADAPTER: Lançamento de Erro se Nulo
	 * ========================================================================= */
	@Test(expected = IllegalStateException.class)
	public void deveLancarErroEmBarrasSemAdapter() {
		EvoluVendasBarras barras = new EvoluVendasBarras();
		barras.processarDados();
	}

	@Test(expected = IllegalStateException.class)
	public void deveLancarErroEmLinhaSemAdapter() {
		EvoluVendasLinha linha = new EvoluVendasLinha();
		linha.processarDados();
	}

	/* =========================================================================
	 * VALIDAÇÃO DO ADAPTER EM MEMÓRIA: Independência do Banco de Dados Firebird
	 * ========================================================================= */
	@Test
	public void deveProcessarBarrasComAdapterEmMemoria() {
		MemoriaVendasAdapter adapter = new MemoriaVendasAdapter();
		adapter.adicionarRegistro(1, "2026", 1500.50)
		       .adicionarRegistro(2, "2026", 2300.00);

		EvoluVendasBarras barras = new EvoluVendasBarras();
		barras.setDataAdapter(adapter);
		barras.processarDados();

		DefaultCategoryDataset dataset = barras.getDatasetBar();
		assertEquals(2, dataset.getRowCount());
		assertEquals(1500.50, ((Number) dataset.getValue(0, 0)).doubleValue(), 0.001);
		assertEquals(2300.00, ((Number) dataset.getValue(1, 0)).doubleValue(), 0.001);

		JFreeChart chart = barras.createChart(dataset);
		assertNotNull("JFreeChart 3D deve ser gerado a partir do dataset adaptado", chart);
	}

	@Test
	public void deveProcessarLinhaComAdapterEmMemoria() {
		MemoriaVendasAdapter adapter = new MemoriaVendasAdapter();
		adapter.adicionarRegistro(3, "2026", 3100.00)
		       .adicionarRegistro(4, "2026", 4200.00);

		EvoluVendasLinha linha = new EvoluVendasLinha();
		linha.setDataAdapter(adapter);
		linha.processarDados();

		XYSeriesCollection dataset = linha.getDatasetLinha();
		assertEquals(1, dataset.getSeriesCount());
		assertEquals(2, dataset.getSeries(0).getItemCount());
		assertEquals(2, linha.getVLegenda().size());

		JFreeChart chart = linha.createXYLineChart(dataset);
		assertNotNull("JFreeChart de linha deve ser gerado a partir do dataset adaptado", chart);
	}

	@Test
	public void deveAdaptarResultSetParaRegistros() throws SQLException {
		List<Object[]> linhas = new ArrayList<Object[]>();
		linhas.add(new Object[] { 5000.00, 5, "2026" });

		ResultSet rs = criarResultSetMock(linhas);
		VendasDataAdapter adapter = new ResultSetVendasAdapter(rs);

		List<RegistroVenda> registros = adapter.carregarRegistros();
		assertEquals(1, registros.size());
		RegistroVenda reg = registros.get(0);
		assertEquals(5, reg.getMes());
		assertEquals("2026", reg.getAno());
		assertEquals(5000.00, reg.getValor(), 0.001);
		assertTrue(reg.getRotuloFormatado().contains("Mai"));
	}
}
