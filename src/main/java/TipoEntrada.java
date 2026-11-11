public enum TipoEntrada {
    GENERAL(15000),
    ESTUDIANTE(12000),
    NINO(10000),
    ADULTO_MAYOR(11000);

    private double valor;

    TipoEntrada(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
