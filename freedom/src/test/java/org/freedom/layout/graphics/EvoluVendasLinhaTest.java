package org.freedom.layout.graphics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

public class EvoluVendasLinhaTest {

    @Test
    public void deveCriarGraficoDeLinhaAposRefatoracao() throws Exception {

        EvoluVendasLinha relatorio =
                new EvoluVendasLinha();

        Field campoDataset =
                EvoluVendasLinha.class
                        .getDeclaredField("datasetLinha");

        campoDataset.setAccessible(true);

        XYSeriesCollection dataset =
                (XYSeriesCollection) campoDataset.get(relatorio);

        XYSeries serie =
                new XYSeries("Vendas");

        serie.add(1.0, 100.0);
        serie.add(2.0, 200.0);

        dataset.addSeries(serie);

        Method metodo =
                EvoluVendasLinha.class
                        .getDeclaredMethod("criarGrafico");

        metodo.setAccessible(true);

        JFreeChart chart =
                (JFreeChart) metodo.invoke(relatorio);

        assertNotNull(chart);

        assertTrue(
                chart.getPlot() instanceof XYPlot
        );

        XYPlot plot =
                (XYPlot) chart.getPlot();

        assertNotNull(plot.getDataset());
    }
}