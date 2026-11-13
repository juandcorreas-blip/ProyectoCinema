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
}