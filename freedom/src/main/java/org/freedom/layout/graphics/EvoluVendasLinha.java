package org.freedom.layout.graphics;

import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.library.functions.Funcoes;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EvoluVendasLinha
        extends RelatorioEvolucaoVendas {

    private static final long serialVersionUID = 1L;

    private final Font fnLegenda =
            new Font(
                    "Arial",
                    Font.PLAIN,
                    8
            );

    private final XYSeriesCollection datasetLinha =
            new XYSeriesCollection();

    private final Vector<String> vLegenda =
            new Vector<String>();

    @Override
    protected void processarDados() {

        XYSeries serie =
                new XYSeries("Vendas");

        try {

            int indice = 1;

            while (rs.next()) {

                serie.add(
                        indice,
                        rs.getDouble("VLR")
                );

                vLegenda.add(
                        rs.getString("MESANO")
                );

                indice++;
            }

            datasetLinha.addSeries(serie);
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
                ChartFactory.createXYLineChart(
                        "",
                        "",
                        "Valor",
                        datasetLinha,
                        PlotOrientation.VERTICAL,
                        false,
                        true,
                        false
                );

        chart.setBackgroundPaint(
                Color.WHITE
        );

        return chart;
    }

    @Override
    protected void desenharRelatorio(
            JFreeChart chart
    ) {

        int iY =
                desenharCabecalhoEvolucaoVendas();

        iY += 30;

        drawGrafico(
                chart,
                15,
                iY,
                500,
                400
        );

        iY += 410;

        setFonte(fnLegenda);

        for (
                int i = 0;
                i < vLegenda.size();
                i++
        ) {

            drawTexto(
                    (i + 1)
                            + " - "
                            + vLegenda.get(i),
                    20,
                    iY,
                    120,
                    AL_LL
            );

            iY += 10;
        }
    }
}