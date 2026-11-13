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
}