package org.freedom.layout.graphics.strategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AgrupamentoPorPercentualTest {

    @Test
    public void deveAgruparValorAbaixoDeTresPorCento() {
        AgrupamentoPorPercentual estrategia =
                new AgrupamentoPorPercentual(3.0);

        assertTrue(estrategia.deveAgrupar(1.0, 100.0));
    }

    @Test
    public void deveAgruparValorDeDoisVirgulaNoventaENovePorCento() {
        AgrupamentoPorPercentual estrategia =
                new AgrupamentoPorPercentual(3.0);

        assertTrue(estrategia.deveAgrupar(2.99, 100.0));
    }

    @Test
    public void naoDeveAgruparValorIgualATresPorCento() {
        AgrupamentoPorPercentual estrategia =
                new AgrupamentoPorPercentual(3.0);

        assertFalse(estrategia.deveAgrupar(3.0, 100.0));
    }

    @Test
    public void naoDeveAgruparValorAcimaDeTresPorCento() {
        AgrupamentoPorPercentual estrategia =
                new AgrupamentoPorPercentual(3.0);

        assertFalse(estrategia.deveAgrupar(10.0, 100.0));
    }
}
