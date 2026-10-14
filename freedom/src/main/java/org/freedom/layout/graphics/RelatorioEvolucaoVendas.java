package org.freedom.layout.graphics;

import java.awt.Font;
import java.sql.ResultSet;
import java.util.Vector;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.LeiauteGR;
import org.jfree.chart.JFreeChart;

public abstract class RelatorioEvolucaoVendas extends LeiauteGR {

    private static final long serialVersionUID = 1L;

    protected DbConnection con = null;
    protected ResultSet rs = null;

    protected Font fnTopEmp =
            new Font("Arial", Font.BOLD, 11);

    protected Font fnCabEmp =
            new Font("Arial", Font.PLAIN, 8);

    protected Font fnCabEmpNeg =
            new Font("Arial", Font.BOLD, 8);

    protected Vector<?> vParamOrc =
            new Vector<Object>();

    /*
     * TEMPLATE METHOD
     *
     * Define a sequência fixa de geração do relatório.
     */
    public final void montaG() {

        impRaz(false);

        montaCabEmp(con);

        montaRelatorio();
    }

    /*
     * Fluxo comum dos relatórios de evolução de vendas.
     */
    protected final void montaRelatorio() {

        imprimeRodape(false);

        processarDados();

        JFreeChart chart = criarGrafico();

        desenharRelatorio(chart);

        termPagina();

        finaliza();
    }

    /*
     * Etapas variáveis implementadas pelas subclasses.
     */

    protected abstract void processarDados();

    protected abstract JFreeChart criarGrafico();

    protected abstract void desenharRelatorio(
            JFreeChart chart
    );

    /*
     * Cabeçalho comum aos dois relatórios.
     */
    protected int desenharCabecalhoEvolucaoVendas() {

        setBordaRel();

        int iY = 35;

        drawLinha(
                0,
                iY,
                0,
                0,
                AL_LL
        );

        iY += 14;

        setFonte(fnTopEmp);

        drawTexto(
                "EVOLUÇÃO DE VENDAS",
                0,
                iY,
                getFontMetrics(fnCabEmp)
                        .stringWidth(
                                "  EVOLUÇÃO DE VENDAS  "
                        ),
                AL_CEN
        );

        setFonte(fnCabEmpNeg);

        iY += 6;

        drawLinha(
                0,
                iY,
                0,
                0,
                AL_LL
        );

        return iY;
    }

    public void setParam(Vector<?> vParam) {
        this.vParamOrc = vParam;
    }

    public void setConsulta(ResultSet rs) {
        this.rs = rs;
    }

    public void setConexao(DbConnection con) {
        this.con = con;
    }
}