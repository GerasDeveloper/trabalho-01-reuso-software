package org.freedom.layout.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;

public class EvoluVendasBarrasTest {

    @Test
    public void deveCriarGraficoDeBarrasComDatasetInformado()
            throws Exception {

        EvoluVendasBarras relatorio =
                new EvoluVendasBarras();

        DefaultCategoryDataset dataset =
                new DefaultCategoryDataset();

        dataset.addValue(
            100.0,
            "Janeiro",
            ""
        );

        Method metodo = EvoluVendasBarras.class
                .getDeclaredMethod(
                    "createChart",
                    CategoryDataset.class
                );

        metodo.setAccessible(true);

        JFreeChart chart =
                (JFreeChart) metodo.invoke(
                    relatorio,
                    dataset
                );

        assertNotNull(chart);

        assertTrue(
            chart.getPlot() instanceof CategoryPlot
        );

        CategoryPlot plot =
                (CategoryPlot) chart.getPlot();

        assertSame(
            dataset,
            plot.getDataset()
        );

        assertEquals(
            0.6f,
            plot.getForegroundAlpha(),
            0.001f
        );
    }
}