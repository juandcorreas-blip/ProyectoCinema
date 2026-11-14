import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SalaTest {

    @Test
    void generarAsientosCreaTantosAsientosComoLaCapacidad() {
        Sala sala = new Sala("S1", "Sala 1", 25);
        sala.generarAsientos(10);
        assertEquals(25, sala.getAsientos().size());
    }

    @Test
    void generarAsientosNombraFilasYNumeros() {
        Sala sala = new Sala("S1", "Sala 1", 25);
        sala.generarAsientos(10);
        assertEquals("A1", sala.getAsientos().get(0).getCodigo());
        assertEquals("A10", sala.getAsientos().get(9).getCodigo());
        assertEquals("B1", sala.getAsientos().get(10).getCodigo());
        assertEquals("C5", sala.getAsientos().get(24).getCodigo());
    }

    @Test
    void losAsientosGeneradosEmpiezanDisponibles() {
        Sala sala = new Sala("S1", "Sala 1", 5);
        sala.generarAsientos(10);
        for (Asiento a : sala.getAsientos()) {
            assertEquals(EstadoAsiento.DISPONIBLE, a.getEstado());
        }
    }

    @Test
    void generarAsientosDosVecesNoDuplicaLosAsientos() {
        Sala sala = new Sala("S1", "Sala 1", 12);
        sala.generarAsientos(10);
        sala.generarAsientos(10);
        assertEquals(12, sala.getAsientos().size());
    }
}
