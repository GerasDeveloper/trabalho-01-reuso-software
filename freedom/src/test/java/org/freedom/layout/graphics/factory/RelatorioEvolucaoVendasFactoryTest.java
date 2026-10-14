package org.freedom.layout.graphics.factory;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.freedom.layout.graphics.EvoluVendasBarras;
import org.freedom.layout.graphics.EvoluVendasLinha;
import org.freedom.library.component.LeiauteGR;
import org.junit.Test;

/**
 * Testes unitários para o padrão GoF Factory Method (RelatorioEvolucaoVendasFactory).
 */
public class RelatorioEvolucaoVendasFactoryTest {

	@Test
	public void deveCriarBarrasComCodigo() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("B");
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasBarras);
	}

	@Test
	public void deveCriarBarrasComCodigoMinusculo() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("b");
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasBarras);
	}

	@Test
	public void deveCriarBarrasComEnum() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio(TipoGraficoVendas.BARRAS);
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasBarras);
	}

	@Test
	public void deveCriarLinhasComCodigo() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("L");
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasLinha);
	}

	@Test
	public void deveCriarLinhasComCodigoMinusculo() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("l");
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasLinha);
	}

	@Test
	public void deveCriarLinhasComEnum() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio(TipoGraficoVendas.LINHAS);
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasLinha);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveFalharComCodigoInvalido() {
		RelatorioEvolucaoVendasFactory.criarRelatorio("X");
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveFalharComCodigoNulo() {
		RelatorioEvolucaoVendasFactory.criarRelatorio((String) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deveFalharComEnumNulo() {
		RelatorioEvolucaoVendasFactory.criarRelatorio((TipoGraficoVendas) null);
	}

	@Test
	public void deveCriarBarrasComParametrosDeExecucao() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("B", null, null);
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasBarras);
	}

	@Test
	public void deveCriarLinhasComParametrosDeExecucao() {
		LeiauteGR relatorio = RelatorioEvolucaoVendasFactory.criarRelatorio("L", null, null);
		assertNotNull(relatorio);
		assertTrue(relatorio instanceof EvoluVendasLinha);
	}
}
