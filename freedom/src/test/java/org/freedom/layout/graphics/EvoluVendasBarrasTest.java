package org.freedom.layout.graphics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

public class EvoluVendasBarrasTest {

    @Test
    public void deveCriarGraficoDeBarrasAposRefatoracao() throws Exception {

        EvoluVendasBarras relatorio = new EvoluVendasBarras();

        Field campoDataset = EvoluVendasBarras.class
                .getDeclaredField("datasetBar");

        campoDataset.setAccessible(true);

        DefaultCategoryDataset dataset =
                (DefaultCategoryDataset) campoDataset.get(relatorio);

        dataset.addValue(100.0, "Vendas", "01/2026");
        dataset.addValue(200.0, "Vendas", "02/2026");

        Method metodo = EvoluVendasBarras.class
                .getDeclaredMethod("criarGrafico");

        metodo.setAccessible(true);

        JFreeChart chart =
                (JFreeChart) metodo.invoke(relatorio);

        assertNotNull(chart);

        assertTrue(
                chart.getPlot() instanceof CategoryPlot
        );

        CategoryPlot plot =
                (CategoryPlot) chart.getPlot();

        assertNotNull(plot.getDataset());
    }
}