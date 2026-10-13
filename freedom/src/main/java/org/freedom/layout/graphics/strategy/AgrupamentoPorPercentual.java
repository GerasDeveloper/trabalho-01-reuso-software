package org.freedom.layout.graphics.strategy;

public class AgrupamentoPorPercentual implements EstrategiaAgrupamento {

    private final double percentualMinimo;

    public AgrupamentoPorPercentual(double percentualMinimo) {
        this.percentualMinimo = percentualMinimo;
    }

    @Override
    public boolean deveAgrupar(double valor, double total) {
        double percentual = (valor * 100) / total;
        return percentual < percentualMinimo;
    }
}