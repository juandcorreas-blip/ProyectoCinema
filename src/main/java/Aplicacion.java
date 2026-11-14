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

    // ---------------- Datos iniciales ----------------
    /** Deja algunos datos cargados para poder probar el menu desde el inicio. */
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

    // ---------------- Menu principal ----------------
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

    // ---------------- Apoyo para mostrar ----------------
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

    // ---------------- Lectura de datos ----------------
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

    /** Si recibe una funcion muestra el valor que pagaria el cliente; si es null muestra la tarifa definida. */
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