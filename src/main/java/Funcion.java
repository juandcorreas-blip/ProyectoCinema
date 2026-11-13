import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Funcion {

    private String codigo;
    private Pelicula pelicula;
    private Sala sala;
    private LocalDate fecha;
    private LocalTime hora;
    private double valorBase;
    private EstadoFuncion estado;
    private List<Asiento> asientos;

    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha, LocalTime hora, double valorBase) {
        this.codigo = codigo;
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.hora = hora;
        this.valorBase = valorBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.asientos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public double getValorBase() {
        return valorBase;
    }

    public void setValorBase(double valorBase) {
        this.valorBase = valorBase;
    }

    public EstadoFuncion getEstado() {
        return estado;
    }

    public void setEstado(EstadoFuncion estado) {
        this.estado = estado;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    public void generarAsientos() {
        asientos.clear();
        for (Asiento a : sala.getAsientos()) {
            asientos.add(new Asiento(a.getCodigo(), a.getFila(), a.getNumero()));
        }
    }

    public Asiento buscarAsiento(String codigo) {
        for (Asiento a : asientos) {
            if (a.getCodigo().equalsIgnoreCase(codigo)) {
                return a;
            }
        }
        return null;
    }

    public int contarAsientosDisponibles() {
        int total = 0;
        for (Asiento a : asientos) {
            if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                total++;
            }
        }
        return total;
    }

    public int contarAsientosOcupados() {
        return asientos.size() - contarAsientosDisponibles();
    }

    public List<Asiento> consultarAsientosDisponibles() {
        List<Asiento> disponibles = new ArrayList<>();
        for (Asiento a : asientos) {
            if (a.getEstado() == EstadoAsiento.DISPONIBLE) {
                disponibles.add(a);
            }
        }
        return disponibles;
    }

    public double calcularValorEntrada(TipoEntrada tipo) {
        return valorBase * tipo.getValor() / TipoEntrada.GENERAL.getValor();
    }

    @Override
    public String toString() {
        return codigo + " | " + pelicula.getNombre() + " | " + sala.getNombre() + " | " + fecha + " | " + hora
                + " | $" + valorBase + " | " + contarAsientosDisponibles() + " disponibles";
    }

}