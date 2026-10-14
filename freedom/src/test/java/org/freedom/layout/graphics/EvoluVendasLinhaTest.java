package org.freedom.layout.graphics;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

public class EvoluVendasLinhaTest {

    @Test
    public void deveCriarGraficoDeLinhaComDatasetInformado() throws Exception {

        EvoluVendasLinha relatorio = new EvoluVendasLinha();

        XYSeries serie = new XYSeries("Vendas");
        serie.add(1.0, 100.0);
        serie.add(2.0, 200.0);
        serie.add(3.0, 150.0);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serie);

        Method metodo = EvoluVendasLinha.class
                .getDeclaredMethod(
                        "createXYLineChart",
                        XYSeriesCollection.class
                );

        metodo.setAccessible(true);

        JFreeChart chart = (JFreeChart) metodo.invoke(
                relatorio,
                dataset
        );

        assertNotNull(chart);

        assertTrue(chart.getPlot() instanceof XYPlot);

        XYPlot plot = (XYPlot) chart.getPlot();

        assertSame(
                dataset,
                plot.getDataset()
        );
    }
}