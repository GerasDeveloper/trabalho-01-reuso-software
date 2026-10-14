package org.freedom.layout.graphics;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.freedom.layout.graphics.adapter.RegistroVenda;
import org.freedom.layout.graphics.adapter.ResultSetVendasAdapter;
import org.freedom.layout.graphics.adapter.VendasDataAdapter;
import org.freedom.library.functions.Funcoes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

public class EvoluVendasBarras extends RelatorioEvolucaoVendas {

    private static final long serialVersionUID = 1L;

    private final DefaultCategoryDataset datasetBar = new DefaultCategoryDataset();

    private VendasDataAdapter dataAdapter = null;

    @Override
    public void processarDados() {
        if ( dataAdapter == null ) {
            throw new IllegalStateException( "Fonte de dados de vendas (VendasDataAdapter) não configurada." );
        }

        try {
            List<RegistroVenda> registros = dataAdapter.carregarRegistros();
            for ( RegistroVenda reg : registros ) {
                datasetBar.addValue( reg.getValor(), reg.getRotuloFormatado(), "" );
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
            Funcoes.mensagemInforma( this, "Erro na consulta de valores!\n" + e.getMessage() );
        }
    }

    @Override
    protected JFreeChart criarGrafico() {
        return createChart( datasetBar );
    }

    public JFreeChart createChart( CategoryDataset dataset ) {
        JFreeChart chart = ChartFactory.createBarChart3D(
                "",
                "Meses",
                "Valores",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chart.setBackgroundPaint( Color.WHITE );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint( Color.WHITE );
        plot.setForegroundAlpha( 0.6f );

        return chart;
    }

    @Override
    protected void desenharRelatorio( JFreeChart chart ) {
        int iY = desenharCabecalhoEvolucaoVendas();
        iY += 50;
        drawGrafico( chart, 15, iY, 500, 500 );
    }

    @Override
    public void setConsulta( ResultSet rs2 ) {
        this.rs = rs2;
        this.dataAdapter = rs2 != null ? new ResultSetVendasAdapter( rs2 ) : null;
    }

    public void setDataAdapter( VendasDataAdapter adapter ) {
        this.dataAdapter = adapter;
    }

    public VendasDataAdapter getDataAdapter() {
        return this.dataAdapter;
    }

    public DefaultCategoryDataset getDatasetBar() {
        return this.datasetBar;
    }
}
