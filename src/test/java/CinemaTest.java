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

    // ---------- Ventas ----------
    @Test
    void venderEntradaOcupaElAsientoYCalculaElValor() throws Exception {
        Venta venta = cinema.venderEntrada("FUN-001", ana, TipoEntrada.ESTUDIANTE, "A1");
        assertEquals("VEN-001", venta.getCodigo());
        assertEquals(12000, venta.getValorPagado(), 0.001);
        assertEquals(EstadoVenta.ACTIVA, venta.getEstado());
        assertNotNull(venta.getFechaHoraVenta());
        assertEquals(EstadoAsiento.OCUPADO, funcion1.buscarAsiento("A1").getEstado());
    }

    @Test
    void venderUnAsientoOcupadoLanzaExcepcion() throws Exception {
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        assertThrows(AsientoOcupadoException.class,
                () -> cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1"));
        assertEquals(1, cinema.getVentas().size());
    }

    @Test
    void venderParaFuncionInexistenteLanzaExcepcion() {
        assertThrows(FuncionNoEncontradaException.class,
                () -> cinema.venderEntrada("FUN-999", ana, TipoEntrada.GENERAL, "A1"));
    }

    @Test
    void venderSinAsientosDisponiblesLanzaExcepcion() {
        for (Asiento a : funcion1.getAsientos()) {
            a.setEstado(EstadoAsiento.OCUPADO);
        }
        assertThrows(SinAsientosDisponiblesException.class,
                () -> cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1"));
    }

    @Test
    void venderAClienteNoRegistradoLanzaExcepcion() {
        Cliente desconocido = new Cliente("9999", "Nadie", TipoCliente.GENERAL);
        assertThrows(IllegalArgumentException.class,
                () -> cinema.venderEntrada("FUN-001", desconocido, TipoEntrada.GENERAL, "A1"));
    }

    @Test
    void venderUnAsientoQueNoExisteLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "Z99"));
    }

    @Test
    void laOcupacionEsIndependienteEntreFunciones() throws Exception {
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        assertEquals(1, funcion1.contarAsientosOcupados());
        assertEquals(0, funcion2.contarAsientosOcupados());
        cinema.venderEntrada("FUN-002", ana, TipoEntrada.GENERAL, "A1");
        assertEquals(1, funcion2.contarAsientosOcupados());
    }

    @Test
    void generarEntradaIncluyeLosDatosDeLaVenta() throws Exception {
        Venta venta = cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "B3");
        String entrada = cinema.generarEntrada(venta);
        assertTrue(entrada.contains("Interestelar"));
        assertTrue(entrada.contains("B3"));
        assertTrue(entrada.contains("Ana Perez"));
    }

    // ---------- Cancelacion ----------
    @Test
    void cancelarVentaLiberaElAsientoYCambiaElEstado() throws Exception {
        Venta venta = cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        cinema.cancelarVenta(venta.getCodigo());
        assertEquals(EstadoVenta.CANCELADA, venta.getEstado());
        assertEquals(EstadoAsiento.DISPONIBLE, funcion1.buscarAsiento("A1").getEstado());
    }

    @Test
    void cancelarVentaInexistenteLanzaExcepcion() {
        assertThrows(VentaNoEncontradaException.class, () -> cinema.cancelarVenta("VEN-777"));
    }

    @Test
    void cancelarDosVecesLaMismaVentaLanzaExcepcion() throws Exception {
        Venta venta = cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        cinema.cancelarVenta(venta.getCodigo());
        assertThrows(IllegalStateException.class, () -> cinema.cancelarVenta(venta.getCodigo()));
    }

    @Test
    void despuesDeCancelarElAsientoSePuedeVolverAVender() throws Exception {
        Venta venta = cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        cinema.cancelarVenta(venta.getCodigo());
        Venta nueva = cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        assertEquals(EstadoVenta.ACTIVA, nueva.getEstado());
    }

    // ---------- Reportes ----------
    @Test
    void reportesSinVentasEstanEnCero() {
        assertEquals(0, cinema.calcularTotalEntradasVendidas());
        assertEquals(0, cinema.calcularTotalIngresos(), 0.001);
        assertNull(cinema.obtenerPeliculaMasVendida());
    }

    @Test
    void reportesContabilizanSoloLasVentasActivas() throws Exception {
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.ESTUDIANTE, "A2");
        Venta tercera = cinema.venderEntrada("FUN-002", ana, TipoEntrada.NINO, "A1");
        cinema.cancelarVenta(tercera.getCodigo());
        assertEquals(2, cinema.calcularTotalEntradasVendidas());
        assertEquals(27000, cinema.calcularTotalIngresos(), 0.001);
    }

    @Test
    void peliculaMasVendidaEsLaQueTieneMasEntradasActivas() throws Exception {
        Funcion funcionCoco = cinema.programarFuncion(coco, sala1, LocalDate.of(2026, 11, 22), LocalTime.of(16, 0), 10000);
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A1");
        cinema.venderEntrada("FUN-001", ana, TipoEntrada.GENERAL, "A2");
        cinema.venderEntrada(funcionCoco.getCodigo(), ana, TipoEntrada.GENERAL, "A1");
        assertSame(interestelar, cinema.obtenerPeliculaMasVendida());
        assertEquals(2, cinema.contarEntradasPelicula(interestelar));
        assertEquals(1, cinema.contarEntradasPelicula(coco));
    }

}