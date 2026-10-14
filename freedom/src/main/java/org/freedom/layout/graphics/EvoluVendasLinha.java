package org.freedom.layout.graphics;

import java.awt.Color;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.freedom.layout.graphics.adapter.RegistroVenda;
import org.freedom.layout.graphics.adapter.ResultSetVendasAdapter;
import org.freedom.layout.graphics.adapter.VendasDataAdapter;
import org.freedom.library.functions.Funcoes;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class EvoluVendasLinha extends RelatorioEvolucaoVendas {

    private static final long serialVersionUID = 1L;

    private final Font fnLegenda = new Font( "Arial", Font.BOLD, 10 );

    private final XYSeriesCollection datasetLinha = new XYSeriesCollection();

    private final Vector<String> vLegenda = new Vector<String>();

    private VendasDataAdapter dataAdapter = null;

    @Override
    public void processarDados() {
        if ( dataAdapter == null ) {
            throw new IllegalStateException( "Fonte de dados de vendas (VendasDataAdapter) não configurada." );
        }

        try {
            XYSeries series = new XYSeries( "Evolução de vendas" );
            vLegenda.clear();

            List<RegistroVenda> registros = dataAdapter.carregarRegistros();
            for ( RegistroVenda reg : registros ) {
                series.add( reg.getMes(), reg.getValor() );
                vLegenda.addElement( reg.getRotuloFormatado() );
            }

            datasetLinha.removeAllSeries();
            datasetLinha.addSeries( series );

        } catch ( SQLException e ) {
            e.printStackTrace();
            Funcoes.mensagemInforma( this, "Erro na consulta de valores!\n" + e.getMessage() );
        }
    }

    @Override
    protected JFreeChart criarGrafico() {
        return createXYLineChart( datasetLinha );
    }

    public JFreeChart createXYLineChart( XYSeriesCollection dataset ) {
        JFreeChart chart = ChartFactory.createXYLineChart(
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
        return chart;
    }

    @Override
    protected void desenharRelatorio( JFreeChart chart ) {
        int iY = desenharCabecalhoEvolucaoVendas();
        iY += 30;

        drawGrafico( chart, 15, iY, 500, 400 );

        int ix = 0;
        iY = 525;
        int iSalto = 0;

        setFonte( fnLegenda );
        int iPos = 50;
        for ( int i = 0; vLegenda.size() > i; i++ ) {
            if ( iPos == 50 ) {
                drawTexto( vLegenda.elementAt( i ).toString(), iPos, iY );
                ix = 140;
                iSalto = 0;
            }
            else if ( iPos == 190 ) {
                drawTexto( vLegenda.elementAt( i ).toString(), iPos, iY );
                ix = 140;
                iSalto = 0;
            }
            else if ( iPos == 330 ) {
                drawTexto( vLegenda.elementAt( i ).toString(), iPos, iY );
                ix = -280;
                iSalto = 15;
            }
            iY += iSalto;
            iPos += ix;
        }

        drawRetangulo( 45, 510, 60, iY - 510 - 5, AL_CDIR );
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

    public XYSeriesCollection getDatasetLinha() {
        return this.datasetLinha;
    }

    public Vector<String> getVLegenda() {
        return this.vLegenda;
    }
}
