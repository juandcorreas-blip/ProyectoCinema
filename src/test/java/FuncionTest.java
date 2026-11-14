import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FuncionTest {

    private Sala sala;
    private Funcion funcion;

    @BeforeEach
    void configurar() {
        Pelicula pelicula = new Pelicula("P1", "Interestelar", "Ciencia ficcion", 169, ClasificacionPelicula.MAYORES_12);
        sala = new Sala("S1", "Sala 1", 10);
        sala.generarAsientos(5);
        funcion = new Funcion("FUN-001", pelicula, sala, LocalDate.of(2026, 11, 20), LocalTime.of(14, 0), 15000);
        funcion.generarAsientos();
    }

    @Test
    void generarAsientosCopiaLosAsientosDeLaSala() {
        assertEquals(10, funcion.getAsientos().size());
    }

    @Test
    void ocuparUnAsientoDeLaFuncionNoAfectaALaSala() {
        funcion.buscarAsiento("A1").setEstado(EstadoAsiento.OCUPADO);
        assertEquals(EstadoAsiento.DISPONIBLE, sala.getAsientos().get(0).getEstado());
    }

    @Test
    void contarAsientosDisponiblesYOcupados() {
        funcion.buscarAsiento("A1").setEstado(EstadoAsiento.OCUPADO);
        funcion.buscarAsiento("B2").setEstado(EstadoAsiento.OCUPADO);
        assertEquals(8, funcion.contarAsientosDisponibles());
        assertEquals(2, funcion.contarAsientosOcupados());
    }

    @Test
    void buscarAsientoIgnoraMayusculasYDevuelveNullSiNoExiste() {
        assertNotNull(funcion.buscarAsiento("a1"));
        assertNull(funcion.buscarAsiento("Z9"));
    }

    @Test
    void consultarAsientosDisponiblesNoIncluyeLosOcupados() {
        funcion.buscarAsiento("A1").setEstado(EstadoAsiento.OCUPADO);
        assertEquals(9, funcion.consultarAsientosDisponibles().size());
        assertNull(findCodigo(funcion, "A1"));
    }

    @Test
    void calcularValorEntradaSegunElTipo() {
        assertEquals(15000, funcion.calcularValorEntrada(TipoEntrada.GENERAL), 0.001);
        assertEquals(12000, funcion.calcularValorEntrada(TipoEntrada.ESTUDIANTE), 0.001);
        assertEquals(10000, funcion.calcularValorEntrada(TipoEntrada.NINO), 0.001);
        assertEquals(11000, funcion.calcularValorEntrada(TipoEntrada.ADULTO_MAYOR), 0.001);
    }

    private Asiento findCodigo(Funcion f, String codigo) {
        for (Asiento a : f.consultarAsientosDisponibles()) {
            if (a.getCodigo().equals(codigo)) {
                return a;
            }
        }
        return null;
    }
}
