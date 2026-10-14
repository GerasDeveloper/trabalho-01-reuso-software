package org.freedom.layout.graphics;

import java.awt.Color;
import java.sql.SQLException;

import org.freedom.library.functions.Funcoes;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class EvoluVendasBarras
        extends RelatorioEvolucaoVendas {

    private static final long serialVersionUID = 1L;

    private final DefaultCategoryDataset datasetBar =
            new DefaultCategoryDataset();

    @Override
    protected void processarDados() {

        try {

            while (rs.next()) {

                datasetBar.addValue(
                        rs.getDouble("VLR"),
                        "Vendas",
                        rs.getString("MESANO")
                );
            }

        }
        catch (SQLException err) {

            Funcoes.mensagemErro(
                    this,
                    "Erro ao processar dados!\n"
                            + err.getMessage(),
                    true,
                    con,
                    err
            );
        }
    }

    @Override
    protected JFreeChart criarGrafico() {

        JFreeChart chart =
                ChartFactory.createBarChart3D(
                        "",
                        "",
                        "Valor",
                        datasetBar,
                        PlotOrientation.VERTICAL,
                        false,
                        true,
                        false
                );

        chart.setBackgroundPaint(Color.WHITE);

        CategoryPlot plot =
                (CategoryPlot) chart.getPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setForegroundAlpha(0.6f);

        return chart;
    }

    @Override
    protected void desenharRelatorio(
            JFreeChart chart
    ) {

        int iY =
                desenharCabecalhoEvolucaoVendas();

        iY += 50;

        drawGrafico(
                chart,
                15,
                iY,
                500,
                500
        );
    }
}