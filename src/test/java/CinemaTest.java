import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinemaTest {

    private Cinema cinema;
    private Pelicula interestelar;
    private Pelicula coco;
    private Sala sala1;
    private Cliente ana;
    private Funcion funcion1;
    private Funcion funcion2;

    @BeforeEach
    void configurar() throws Exception {
        cinema = new Cinema("CINEMAUQ");
        interestelar = new Pelicula("P1", "Interestelar", "Ciencia ficcion", 169, ClasificacionPelicula.MAYORES_12);
        coco = new Pelicula("P2", "Coco", "Animacion", 105, ClasificacionPelicula.TODOS);
        cinema.registrarPelicula(interestelar);
        cinema.registrarPelicula(coco);

        sala1 = new Sala("S1", "Sala 1", 20);
        cinema.registrarSala(sala1);

        ana = new Cliente("1001", "Ana Perez", TipoCliente.ESTUDIANTE);
        cinema.registrarCliente(ana);

        funcion1 = cinema.programarFuncion(interestelar, sala1, LocalDate.of(2026, 11, 20), LocalTime.of(14, 0), 15000);
        funcion2 = cinema.programarFuncion(interestelar, sala1, LocalDate.of(2026, 11, 20), LocalTime.of(18, 0), 15000);
    }


    @Test
    void registrarPeliculaConCodigoRepetidoLanzaExcepcion() {
        Pelicula repetida = new Pelicula("P1", "Otra", "Drama", 90, ClasificacionPelicula.TODOS);
        assertThrows(PeliculaDuplicadaException.class, () -> cinema.registrarPelicula(repetida));
        assertEquals(2, cinema.getPeliculas().size());
    }

    @Test
    void modificarPeliculaCambiaSusDatos() {
        assertTrue(cinema.modificarPelicula("P2", "Coco 2", "Aventura", 110, ClasificacionPelicula.MAYORES_7));
        assertEquals("Coco 2", coco.getNombre());
        assertEquals(ClasificacionPelicula.MAYORES_7, coco.getClasificacion());
    }

    @Test
    void modificarPeliculaInexistenteDevuelveFalse() {
        assertFalse(cinema.modificarPelicula("P99", "x", "x", 1, ClasificacionPelicula.TODOS));
    }

    @Test
    void deshabilitarPeliculaLaSacaDeLasDisponibles() {
        assertTrue(cinema.deshabilitarPelicula("P2"));
        assertEquals(EstadoPelicula.NO_DISPONIBLE, coco.getEstado());
        assertEquals(1, cinema.consultarPeliculasDisponibles().size());
    }


    @Test
    void registrarSalaConCodigoRepetidoLanzaExcepcion() {
        Sala repetida = new Sala("S1", "Otra sala", 10);
        assertThrows(SalaDuplicadaException.class, () -> cinema.registrarSala(repetida));
        assertEquals(1, cinema.getSalas().size());
    }

    @Test
    void registrarSalaGeneraSusAsientos() {
        assertEquals(20, sala1.getAsientos().size());
    }

    @Test
    void deshabilitarSalaLaDejaFueraDeServicio() {
        assertTrue(cinema.deshabilitarSala("S1"));
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, sala1.getEstado());
        assertFalse(cinema.deshabilitarSala("S99"));
    }

    @Test
    void registrarClienteRepetidoDevuelveFalse() {
        assertFalse(cinema.registrarCliente(new Cliente("1001", "Otra Persona", TipoCliente.GENERAL)));
        assertEquals(1, cinema.getClientes().size());
    }


    @Test
    void programarFuncionAsignaCodigoYAsientos() {
        assertEquals("FUN-001", funcion1.getCodigo());
        assertEquals("FUN-002", funcion2.getCodigo());
        assertEquals(20, funcion1.getAsientos().size());
        assertEquals(EstadoFuncion.PROGRAMADA, funcion1.getEstado());
    }

    @Test
    void programarFuncionEnSalaFueraDeServicioLanzaExcepcion() {
        cinema.deshabilitarSala("S1");
        assertThrows(SalaNoDisponibleException.class,
                () -> cinema.programarFuncion(coco, sala1, LocalDate.of(2026, 11, 21), LocalTime.of(9, 0), 10000));
        assertEquals(2, cinema.getFunciones().size());
    }

    @Test
    void consultarFuncionesPorFechaSoloDevuelveLasDeEsaFecha() throws Exception {
        cinema.programarFuncion(coco, sala1, LocalDate.of(2026, 11, 21), LocalTime.of(9, 0), 10000);
        assertEquals(2, cinema.consultarFuncionesPorFecha(LocalDate.of(2026, 11, 20)).size());
        assertEquals(1, cinema.consultarFuncionesPorFecha(LocalDate.of(2026, 11, 21)).size());
        assertEquals(0, cinema.consultarFuncionesPorFecha(LocalDate.of(2026, 12, 1)).size());
    }

    @Test
    void obtenerFuncionInexistenteLanzaExcepcion() throws Exception {
        assertSame(funcion1, cinema.obtenerFuncion("FUN-001"));
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.obtenerFuncion("FUN-999"));
    }

}