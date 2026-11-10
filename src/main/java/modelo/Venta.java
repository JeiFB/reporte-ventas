package modelo;

/** Representa un registro de venta leido del archivo CSV. */
public class Venta {

    private final String fecha;
    private final String producto;
    private final int cantidad;
    private final long valor;

    public Venta(String fecha, String producto, int cantidad, long valor) {
        this.fecha = fecha;
        this.producto = producto;
        this.cantidad = cantidad;
        this.valor = valor;
    }

    public String getFecha() {
        return fecha;
    }

    public String getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public long getValor() {
        return valor;
    }
}
