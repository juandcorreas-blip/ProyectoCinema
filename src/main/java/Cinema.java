import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Cinema {

    private String nombre;
    private List<Pelicula> peliculas;
    private List<Sala> salas;
    private List<Funcion> funciones;
    private List<Cliente> clientes;
    private List<Venta> ventas;

    public Cinema(String nombre) {
        this.nombre = nombre;
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.ventas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pelicula> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(List<Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void setSalas(List<Sala> salas) {
        this.salas = salas;
    }

    public List<Funcion> getFunciones() {
        return funciones;
    }

    public void setFunciones(List<Funcion> funciones) {
        this.funciones = funciones;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    private static final int ASIENTOS_POR_FILA = 10;
    private int contadorFunciones = 0;
    private int contadorVentas = 0;

    public Pelicula buscarPelicula(String codigo) {
        for (Pelicula p : peliculas) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Sala buscarSala(String codigo) {
        for (Sala s : salas) {
            if (s.getCodigo().equalsIgnoreCase(codigo)) {
                return s;
            }
        }
        return null;
    }

    public Funcion buscarFuncion(String codigo) {
        for (Funcion f : funciones) {
            if (f.getCodigo().equalsIgnoreCase(codigo)) {
                return f;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String identificacion) {
        for (Cliente c : clientes) {
            if (c.getIdentificacion().equalsIgnoreCase(identificacion)) {
                return c;
            }
        }
        return null;
    }

    public Venta buscarVenta(String codigo) {
        for (Venta v : ventas) {
            if (v.getCodigo().equalsIgnoreCase(codigo)) {
                return v;
            }
        }
        return null;
    }
    public Funcion obtenerFuncion(String codigo) throws FuncionNoEncontradaException {
        Funcion funcion = buscarFuncion(codigo);
        if (funcion == null) {
            throw new FuncionNoEncontradaException("No existe una funcion con el codigo " + codigo + ".");
        }
        return funcion;
    }

    public void registrarPelicula(Pelicula pelicula) throws PeliculaDuplicadaException {
        if (pelicula == null) {
            throw new IllegalArgumentException("La pelicula no puede ser nula.");
        }
        if (buscarPelicula(pelicula.getCodigo()) != null) {
            throw new PeliculaDuplicadaException("Ya existe una pelicula con el codigo " + pelicula.getCodigo() + ".");
        }
        peliculas.add(pelicula);
    }

    public boolean modificarPelicula(String codigo, String nombre, String genero, int duracion, ClasificacionPelicula clasificacion) {
        Pelicula pelicula = buscarPelicula(codigo);
        if (pelicula == null) {
            return false;
        }
        pelicula.setNombre(nombre);
        pelicula.setGenero(genero);
        pelicula.setDuracion(duracion);
        pelicula.setClasificacion(clasificacion);
        return true;
    }

    public boolean deshabilitarPelicula(String codigo) {
        Pelicula pelicula = buscarPelicula(codigo);
        if (pelicula == null) {
            return false;
        }
        pelicula.setEstado(EstadoPelicula.NO_DISPONIBLE);
        return true;
    }

    public List<Pelicula> consultarPeliculasDisponibles() {
        List<Pelicula> disponibles = new ArrayList<>();
        for (Pelicula p : peliculas) {
            if (p.getEstado() == EstadoPelicula.DISPONIBLE) {
                disponibles.add(p);
            }
        }
        return disponibles;
    }

    public void registrarSala(Sala sala) throws SalaDuplicadaException {
        if (sala == null) {
            throw new IllegalArgumentException("La sala no puede ser nula.");
        }
        if (buscarSala(sala.getCodigo()) != null) {
            throw new SalaDuplicadaException("Ya existe una sala con el codigo " + sala.getCodigo() + ".");
        }
        if (sala.getAsientos().isEmpty()) {
            sala.generarAsientos(ASIENTOS_POR_FILA);
        }
        salas.add(sala);
    }

    public boolean modificarSala(String codigo, String nombre, int capacidad) {
        Sala sala = buscarSala(codigo);
        if (sala == null) {
            return false;
        }
        sala.setNombre(nombre);
        sala.setCapacidad(capacidad);
        sala.generarAsientos(ASIENTOS_POR_FILA);
        return true;
    }

    public boolean deshabilitarSala(String codigo) {
        Sala sala = buscarSala(codigo);
        if (sala == null) {
            return false;
        }
        sala.setEstado(EstadoSala.FUERA_DE_SERVICIO);
        return true;
    }


    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || buscarCliente(cliente.getIdentificacion()) != null) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }


    public Funcion programarFuncion(Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase)
            throws SalaNoDisponibleException {
        if (pelicula == null || sala == null) {
            throw new IllegalArgumentException("La pelicula y la sala no pueden ser nulas.");
        }
        if (sala.getEstado() == EstadoSala.FUERA_DE_SERVICIO) {
            throw new SalaNoDisponibleException("La sala " + sala.getNombre() + " esta fuera de servicio.");
        }
        if (pelicula.getEstado() == EstadoPelicula.NO_DISPONIBLE) {
            throw new IllegalArgumentException("La pelicula " + pelicula.getNombre() + " no esta disponible.");
        }
        contadorFunciones++;
        String codigo = String.format("FUN-%03d", contadorFunciones);
        Funcion funcion = new Funcion(codigo, pelicula, sala, fecha, hora, valorBase);
        funcion.generarAsientos();
        funciones.add(funcion);
        return funcion;
    }

    public List<Funcion> consultarFuncionesProgramadas() {
        List<Funcion> programadas = new ArrayList<>();
        for (Funcion f : funciones) {
            if (f.getEstado() == EstadoFuncion.PROGRAMADA) {
                programadas.add(f);
            }
        }
        return programadas;
    }

    public List<Funcion> consultarFuncionesPorFecha(LocalDate fecha) {
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion f : funciones) {
            if (f.getEstado() == EstadoFuncion.PROGRAMADA && f.getFecha().equals(fecha)) {
                resultado.add(f);
            }
        }
        return resultado;
    }


    public Venta venderEntrada(String codigoFuncion, Cliente cliente, TipoEntrada tipoEntrada, String codigoAsiento)
            throws FuncionNoEncontradaException, SinAsientosDisponiblesException, AsientoOcupadoException {
        Funcion funcion = obtenerFuncion(codigoFuncion);
        if (funcion.getEstado() != EstadoFuncion.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden vender entradas de funciones programadas.");
        }
        if (cliente == null || buscarCliente(cliente.getIdentificacion()) == null) {
            throw new IllegalArgumentException("El cliente no esta registrado.");
        }
        if (funcion.contarAsientosDisponibles() == 0) {
            throw new SinAsientosDisponiblesException("La funcion " + funcion.getCodigo() + " no tiene asientos disponibles.");
        }
        Asiento asiento = funcion.buscarAsiento(codigoAsiento);
        if (asiento == null) {
            throw new IllegalArgumentException("El asiento " + codigoAsiento + " no existe en la sala.");
        }
        if (asiento.getEstado() == EstadoAsiento.OCUPADO) {
            throw new AsientoOcupadoException("El asiento " + asiento.getCodigo() + " ya esta ocupado.");
        }
        double valor = funcion.calcularValorEntrada(tipoEntrada);
        asiento.setEstado(EstadoAsiento.OCUPADO);
        contadorVentas++;
        String codigo = String.format("VEN-%03d", contadorVentas);
        Venta venta = new Venta(codigo, cliente, funcion, asiento, LocalDateTime.now(), valor);
        ventas.add(venta);
        return venta;
    }


    public String generarEntrada(Venta venta) {
        Funcion f = venta.getFuncion();
        return "===== ENTRADA " + venta.getCodigo() + " =====\n"
                + "Pelicula: " + f.getPelicula().getNombre() + "\n"
                + "Sala: " + f.getSala().getNombre() + "\n"
                + "Fecha: " + f.getFecha() + "  Hora: " + f.getHora() + "\n"
                + "Asiento: " + venta.getAsiento().getCodigo() + "\n"
                + "Cliente: " + venta.getCliente().getNombre() + "\n"
                + "Valor pagado: $" + venta.getValorPagado() + "\n"
                + "Fecha de venta: " + venta.getFechaHoraVenta();
    }

    public void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException {
        Venta venta = buscarVenta(codigoVenta);
        if (venta == null) {
            throw new VentaNoEncontradaException("No existe una venta con el codigo " + codigoVenta + ".");
        }
        if (venta.getEstado() == EstadoVenta.CANCELADA) {
            throw new IllegalStateException("La venta " + codigoVenta + " ya esta cancelada.");
        }
        venta.getAsiento().setEstado(EstadoAsiento.DISPONIBLE);
        venta.setEstado(EstadoVenta.CANCELADA);
    }
    public int calcularTotalEntradasVendidas() {
        int total = 0;
        for (Venta v : ventas) {
            if (v.getEstado() == EstadoVenta.ACTIVA) {
                total++;
            }
        }
        return total;
    }

    public double calcularTotalIngresos() {
        double total = 0;
        for (Venta v : ventas) {
            if (v.getEstado() == EstadoVenta.ACTIVA) {
                total += v.getValorPagado();
            }
        }
        return total;
    }

    public int contarEntradasPelicula(Pelicula pelicula) {
        int cantidad = 0;
        for (Venta v : ventas) {
            if (v.getEstado() == EstadoVenta.ACTIVA
                    && v.getFuncion().getPelicula().getCodigo().equalsIgnoreCase(pelicula.getCodigo())) {
                cantidad++;
            }
        }
        return cantidad;
    }


    public Pelicula obtenerPeliculaMasVendida() {
        Pelicula masVendida = null;
        int maximo = 0;
        for (Pelicula p : peliculas) {
            int cantidad = contarEntradasPelicula(p);
            if (cantidad > maximo) {
                maximo = cantidad;
                masVendida = p;
            }
        }
        return masVendida;
    }


}