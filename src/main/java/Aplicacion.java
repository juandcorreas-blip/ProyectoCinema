import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Aplicacion {

    private static Scanner scanner = new Scanner(System.in);
    private static Cinema cinema = new Cinema("CINEMAUQ");

    public static void main(String[] args) {
        cargarDatosIniciales();
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1:
                        venderEntrada();
                        break;
                    case 2:
                        cancelarVenta();
                        break;
                    case 3:
                        consultarPeliculas();
                        break;
                    case 4:
                        consultarFunciones();
                        break;
                    case 5:
                        consultarDisponibilidad();
                        break;
                    case 6:
                        administrarPeliculas();
                        break;
                    case 7:
                        administrarFunciones();
                        break;
                    case 8:
                        administrarSalas();
                        break;
                    case 9:
                        generarReportes();
                        break;
                    case 10:
                        System.out.println("Hasta pronto.");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 10);
    }


    private static void cargarDatosIniciales() {
        try {
            cinema.registrarPelicula(new Pelicula("P1", "Interestelar", "Ciencia ficcion", 169, ClasificacionPelicula.MAYORES_12));
            cinema.registrarPelicula(new Pelicula("P2", "Coco", "Animacion", 105, ClasificacionPelicula.TODOS));
            cinema.registrarPelicula(new Pelicula("P3", "El Ultimo Algoritmo", "Suspenso", 120, ClasificacionPelicula.MAYORES_15));

            cinema.registrarSala(new Sala("S1", "Sala 1", 30));
            cinema.registrarSala(new Sala("S2", "Sala 2", 20));

            cinema.registrarCliente(new Cliente("1001", "Ana Perez", TipoCliente.ESTUDIANTE));
            cinema.registrarCliente(new Cliente("1002", "Luis Gomez", TipoCliente.ADULTO_MAYOR));

            LocalDate hoy = LocalDate.now();
            cinema.programarFuncion(cinema.buscarPelicula("P1"), cinema.buscarSala("S1"), hoy, LocalTime.of(14, 0), 15000);
            cinema.programarFuncion(cinema.buscarPelicula("P3"), cinema.buscarSala("S2"), hoy, LocalTime.of(18, 30), 15000);
            cinema.programarFuncion(cinema.buscarPelicula("P1"), cinema.buscarSala("S1"), hoy.plusDays(1), LocalTime.of(21, 0), 15000);
        } catch (Exception e) {
            System.out.println("Error al cargar los datos iniciales: " + e.getMessage());
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("==============================");
        System.out.println("          CINEMAUQ");
        System.out.println("==============================");
        System.out.println("1. Venta de entrada");
        System.out.println("2. Cancelar venta");
        System.out.println("3. Consultar peliculas");
        System.out.println("4. Consultar funciones");
        System.out.println("5. Consultar disponibilidad");
        System.out.println("6. Administrar peliculas");
        System.out.println("7. Administrar funciones");
        System.out.println("8. Administrar salas");
        System.out.println("9. Generar reportes");
        System.out.println("10. Salir");
    }

    private static void venderEntrada()
            throws FuncionNoEncontradaException, SinAsientosDisponiblesException, AsientoOcupadoException {
        imprimirLista(cinema.consultarFuncionesProgramadas(), "No hay funciones programadas.");
        Funcion funcion = cinema.obtenerFuncion(leerTexto("Codigo de la funcion: "));

        String identificacion = leerTexto("Identificacion del cliente: ");
        Cliente cliente = cinema.buscarCliente(identificacion);
        if (cliente == null) {
            System.out.println("El cliente no esta registrado, vamos a registrarlo.");
            String nombre = leerTexto("Nombre del cliente: ");
            TipoCliente tipoCliente = leerTipoCliente();
            cliente = new Cliente(identificacion, nombre, tipoCliente);
            cinema.registrarCliente(cliente);
        }

        TipoEntrada tipoEntrada = leerTipoEntrada(funcion);
        System.out.println("Asientos disponibles: " + codigosAsientos(funcion.consultarAsientosDisponibles()));
        String codigoAsiento = leerTexto("Asiento seleccionado: ");

        Venta venta = cinema.venderEntrada(funcion.getCodigo(), cliente, tipoEntrada, codigoAsiento);
        System.out.println("Venta registrada.");
        System.out.println(cinema.generarEntrada(venta));
    }

    private static void cancelarVenta() throws VentaNoEncontradaException {
        String codigo = leerTexto("Codigo de la venta a cancelar: ");
        cinema.cancelarVenta(codigo);
        System.out.println("Venta cancelada y asiento liberado.");
    }

    private static void consultarDisponibilidad() throws FuncionNoEncontradaException {
        imprimirLista(cinema.consultarFuncionesProgramadas(), "No hay funciones programadas.");
        Funcion funcion = cinema.obtenerFuncion(leerTexto("Codigo de la funcion: "));
        mostrarOcupacion(funcion);
        System.out.println();
        for (Asiento a : funcion.getAsientos()) {
            System.out.println(a.getCodigo() + " " + (a.getEstado() == EstadoAsiento.DISPONIBLE ? "Disponible" : "Ocupado"));
        }
    }
    private static void consultarPeliculas() {
        imprimirLista(cinema.consultarPeliculasDisponibles(), "No hay peliculas disponibles.");
    }

    private static void consultarFunciones() {
        imprimirLista(cinema.consultarFuncionesProgramadas(), "No hay funciones programadas.");
    }

    private static void administrarPeliculas() throws PeliculaDuplicadaException {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- Administrar peliculas ---");
            System.out.println("1. Registrar pelicula");
            System.out.println("2. Consultar peliculas");
            System.out.println("3. Modificar pelicula");
            System.out.println("4. Deshabilitar pelicula");
            System.out.println("5. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    String codigo = leerTexto("Codigo: ");
                    String nombre = leerTexto("Nombre: ");
                    String genero = leerTexto("Genero: ");
                    int duracion = leerEntero("Duracion en minutos: ");
                    ClasificacionPelicula clasificacion = leerClasificacion();
                    cinema.registrarPelicula(new Pelicula(codigo, nombre, genero, duracion, clasificacion));
                    System.out.println("Pelicula registrada.");
                    break;
                case 2:
                    imprimirLista(cinema.getPeliculas(), "No hay peliculas registradas.");
                    break;
                case 3:
                    String codigoMod = leerTexto("Codigo de la pelicula a modificar: ");
                    if (cinema.buscarPelicula(codigoMod) == null) {
                        System.out.println("No existe una pelicula con ese codigo.");
                        break;
                    }
                    String nuevoNombre = leerTexto("Nuevo nombre: ");
                    String nuevoGenero = leerTexto("Nuevo genero: ");
                    int nuevaDuracion = leerEntero("Nueva duracion en minutos: ");
                    ClasificacionPelicula nuevaClasificacion = leerClasificacion();
                    cinema.modificarPelicula(codigoMod, nuevoNombre, nuevoGenero, nuevaDuracion, nuevaClasificacion);
                    System.out.println("Pelicula modificada.");
                    break;
                case 4:
                    if (cinema.deshabilitarPelicula(leerTexto("Codigo de la pelicula a deshabilitar: "))) {
                        System.out.println("Pelicula deshabilitada.");
                    } else {
                        System.out.println("No existe una pelicula con ese codigo.");
                    }
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 5);
    }

    private static void administrarFunciones() throws SalaNoDisponibleException {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- Administrar funciones ---");
            System.out.println("1. Programar funcion");
            System.out.println("2. Consultar todas las funciones");
            System.out.println("3. Definir valor de las entradas (tarifas)");
            System.out.println("4. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    imprimirLista(cinema.consultarPeliculasDisponibles(), "No hay peliculas disponibles.");
                    Pelicula pelicula = cinema.buscarPelicula(leerTexto("Codigo de la pelicula: "));
                    imprimirLista(cinema.getSalas(), "No hay salas registradas.");
                    Sala sala = cinema.buscarSala(leerTexto("Codigo de la sala: "));
                    LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
                    LocalTime hora = leerHora("Hora (HH:mm): ");
                    double valorBase = leerDecimal("Valor de la entrada general: ");
                    Funcion funcion = cinema.programarFuncion(pelicula, sala, fecha, hora, valorBase);
                    System.out.println("Funcion programada: " + funcion);
                    break;
                case 2:
                    imprimirLista(cinema.getFunciones(), "No hay funciones registradas.");
                    break;
                case 3:
                    TipoEntrada tipo = leerTipoEntrada(null);
                    tipo.setValor(leerDecimal("Nuevo valor para " + tipo + ": "));
                    System.out.println("Tarifa actualizada.");
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 4);
    }

    private static void administrarSalas() throws SalaDuplicadaException {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- Administrar salas ---");
            System.out.println("1. Registrar sala");
            System.out.println("2. Consultar salas");
            System.out.println("3. Modificar sala");
            System.out.println("4. Deshabilitar sala");
            System.out.println("5. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    String codigo = leerTexto("Codigo: ");
                    String nombre = leerTexto("Nombre o numero: ");
                    int capacidad = leerEntero("Capacidad: ");
                    cinema.registrarSala(new Sala(codigo, nombre, capacidad));
                    System.out.println("Sala registrada.");
                    break;
                case 2:
                    imprimirLista(cinema.getSalas(), "No hay salas registradas.");
                    break;
                case 3:
                    String codigoMod = leerTexto("Codigo de la sala a modificar: ");
                    if (cinema.buscarSala(codigoMod) == null) {
                        System.out.println("No existe una sala con ese codigo.");
                        break;
                    }
                    String nuevoNombre = leerTexto("Nuevo nombre o numero: ");
                    int nuevaCapacidad = leerEntero("Nueva capacidad: ");
                    cinema.modificarSala(codigoMod, nuevoNombre, nuevaCapacidad);
                    System.out.println("Sala modificada.");
                    break;
                case 4:
                    if (cinema.deshabilitarSala(leerTexto("Codigo de la sala a deshabilitar: "))) {
                        System.out.println("Sala fuera de servicio.");
                    } else {
                        System.out.println("No existe una sala con ese codigo.");
                    }
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 5);
    }
    private static void generarReportes() throws FuncionNoEncontradaException {
        int opcion;
        do {
            System.out.println();
            System.out.println("--- Reportes ---");
            System.out.println("1. Total de entradas vendidas");
            System.out.println("2. Total de ingresos");
            System.out.println("3. Pelicula con mayor cantidad de entradas vendidas");
            System.out.println("4. Ocupacion de una funcion");
            System.out.println("5. Funciones programadas en una fecha");
            System.out.println("6. Volver");
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    System.out.println("Entradas vendidas: " + cinema.calcularTotalEntradasVendidas());
                    break;
                case 2:
                    System.out.println("Total de ingresos: $" + cinema.calcularTotalIngresos());
                    break;
                case 3:
                    Pelicula masVendida = cinema.obtenerPeliculaMasVendida();
                    if (masVendida == null) {
                        System.out.println("Todavia no hay entradas vendidas.");
                    } else {
                        System.out.println(masVendida.getNombre() + " con " + cinema.contarEntradasPelicula(masVendida) + " entradas vendidas.");
                    }
                    break;
                case 4:
                    mostrarOcupacion(cinema.obtenerFuncion(leerTexto("Codigo de la funcion: ")));
                    break;
                case 5:
                    LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD): ");
                    imprimirLista(cinema.consultarFuncionesPorFecha(fecha), "No hay funciones programadas para esa fecha.");
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 6);
    }

    private static void mostrarOcupacion(Funcion funcion) {
        System.out.println("Funcion: " + funcion.getPelicula().getNombre());
        System.out.println("Sala: " + funcion.getSala().getNombre());
        System.out.println("Fecha: " + funcion.getFecha() + "  Hora: " + funcion.getHora());
        System.out.println("Total de asientos: " + funcion.getAsientos().size());
        System.out.println("Asientos disponibles: " + funcion.contarAsientosDisponibles());
        System.out.println("Asientos ocupados: " + funcion.contarAsientosOcupados());
    }

    private static String codigosAsientos(List<Asiento> asientos) {
        String texto = "";
        for (Asiento a : asientos) {
            texto += a.getCodigo() + " ";
        }
        return texto.trim();
    }

    private static void imprimirLista(List<?> lista, String mensajeVacio) {
        if (lista.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        for (Object o : lista) {
            System.out.println(o);
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalDate.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use AAAA-MM-DD, por ejemplo 2026-11-20.");
            }
        }
    }

    private static LocalTime leerHora(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return LocalTime.parse(scanner.nextLine().trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use HH:mm, por ejemplo 18:30.");
            }
        }
    }

    private static ClasificacionPelicula leerClasificacion() {
        ClasificacionPelicula[] valores = ClasificacionPelicula.values();
        int opcion;
        do {
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + ". " + valores[i]);
            }
            opcion = leerEntero("Seleccione la clasificacion: ");
        } while (opcion < 1 || opcion > valores.length);
        return valores[opcion - 1];
    }

    private static TipoCliente leerTipoCliente() {
        TipoCliente[] valores = TipoCliente.values();
        int opcion;
        do {
            for (int i = 0; i < valores.length; i++) {
                System.out.println((i + 1) + ". " + valores[i]);
            }
            opcion = leerEntero("Seleccione el tipo de cliente: ");
        } while (opcion < 1 || opcion > valores.length);
        return valores[opcion - 1];
    }


    private static TipoEntrada leerTipoEntrada(Funcion funcion) {
        TipoEntrada[] valores = TipoEntrada.values();
        int opcion;
        do {
            for (int i = 0; i < valores.length; i++) {
                double valor = funcion != null ? funcion.calcularValorEntrada(valores[i]) : valores[i].getValor();
                System.out.println((i + 1) + ". " + valores[i] + " ($" + valor + ")");
            }
            opcion = leerEntero("Seleccione el tipo de entrada: ");
        } while (opcion < 1 || opcion > valores.length);
        return valores[opcion - 1];
    }
}