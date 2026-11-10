package modelo;

/** Indicadores calculados a partir de las ventas. */
public class Indicadores {

    private final long valorTotal;
    private final int cantidadTotal;
    private final String productoMayorValor;
    private final long valorProductoMayor;
    private final double promedioPorRegistro;

    public Indicadores(long valorTotal, int cantidadTotal, String productoMayorValor,
                       long valorProductoMayor, double promedioPorRegistro) {
        this.valorTotal = valorTotal;
        this.cantidadTotal = cantidadTotal;
        this.productoMayorValor = productoMayorValor;
        this.valorProductoMayor = valorProductoMayor;
        this.promedioPorRegistro = promedioPorRegistro;
    }

    public long getValorTotal() {
        return valorTotal;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public String getProductoMayorValor() {
        return productoMayorValor;
    }

    public long getValorProductoMayor() {
        return valorProductoMayor;
    }

    public double getPromedioPorRegistro() {
        return promedioPorRegistro;
    }
}
