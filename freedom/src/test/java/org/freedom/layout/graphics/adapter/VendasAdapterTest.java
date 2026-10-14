package org.freedom.layout.graphics.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.freedom.layout.graphics.EvoluVendasBarras;
import org.freedom.layout.graphics.EvoluVendasLinha;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

/**
 * Teste inicial (código legado SEM refatoração):
 * Exercita DIRETAMENTE as classes EvoluVendasBarras e EvoluVendasLinha
 * garantindo que o código inicial extrai os dados do ResultSet e produz
 * o comportamento X esperado nos respectivos datasets de gráficos.
 */
public class VendasAdapterTest {

	/**
	 * Cria um ResultSet simulado para fornecer dados controlados de vendas
	 * (coluna 1: valor double, coluna 2: mes int, coluna 3: ano String)
	 * sem necessidade de conexão ativa com o banco Firebird legado.
	 */
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

	@Test
	public void deveProcessarDadosEPopularDatasetEmEvoluVendasBarrasOriginal() throws Exception {
		List<Object[]> linhas = new ArrayList<Object[]>();
		linhas.add(new Object[] { 1500.50, 1, "2026" });
		linhas.add(new Object[] { 2300.00, 2, "2026" });

		ResultSet rs = criarResultSetMock(linhas);

		// Instancia a classe original não refatorada
		EvoluVendasBarras barras = new EvoluVendasBarras();
		barras.setConsulta(rs);

		// Invoca o método de montagem do relatório legado
		Method m = EvoluVendasBarras.class.getDeclaredMethod("montaRel");
		m.setAccessible(true);
		m.invoke(barras);

		// Inspeciona o datasetBar original gerado
		Field f = EvoluVendasBarras.class.getDeclaredField("datasetBar");
		f.setAccessible(true);
		DefaultCategoryDataset dataset = (DefaultCategoryDataset) f.get(barras);

		assertEquals(2, dataset.getRowCount());
		assertEquals(1500.50, ((Number) dataset.getValue(0, 0)).doubleValue(), 0.001);
		assertEquals(2300.00, ((Number) dataset.getValue(1, 0)).doubleValue(), 0.001);
		assertTrue(dataset.getRowKey(0).toString().contains("Jan"));
		assertTrue(dataset.getRowKey(0).toString().contains("2026"));
		assertTrue(dataset.getRowKey(1).toString().contains("Fev"));
	}

	@Test
	public void deveProcessarDadosEPopularDatasetEmEvoluVendasLinhaOriginal() throws Exception {
		List<Object[]> linhas = new ArrayList<Object[]>();
		linhas.add(new Object[] { 3100.00, 3, "2026" });
		linhas.add(new Object[] { 4200.00, 4, "2026" });

		ResultSet rs = criarResultSetMock(linhas);

		// Instancia a classe original não refatorada
		EvoluVendasLinha linha = new EvoluVendasLinha();
		linha.setConsulta(rs);

		// Invoca o método de montagem do relatório legado
		Method m = EvoluVendasLinha.class.getDeclaredMethod("montaRel");
		m.setAccessible(true);
		m.invoke(linha);

		// Inspeciona o datasetLinha original gerado
		Field fDataset = EvoluVendasLinha.class.getDeclaredField("datasetLinha");
		fDataset.setAccessible(true);
		XYSeriesCollection dataset = (XYSeriesCollection) fDataset.get(linha);

		assertEquals(1, dataset.getSeriesCount());
		assertEquals(2, dataset.getSeries(0).getItemCount());
		assertEquals(3.0, dataset.getSeries(0).getX(0).doubleValue(), 0.001);
		assertEquals(3100.00, dataset.getSeries(0).getY(0).doubleValue(), 0.001);
		assertEquals(4.0, dataset.getSeries(0).getX(1).doubleValue(), 0.001);
		assertEquals(4200.00, dataset.getSeries(0).getY(1).doubleValue(), 0.001);
	}
}
