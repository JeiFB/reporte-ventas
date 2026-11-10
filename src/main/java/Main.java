import modelo.Indicadores;
import modelo.Venta;
import servicio.CalculadoraIndicadores;
import servicio.LectorVentas;

import java.util.List;

public class Main {

    private static final String RUTA_CSV = "datos/ventas.csv";

    public static void main(String[] args) throws Exception {
        List<Venta> ventas = new LectorVentas().leer(RUTA_CSV);
        Indicadores indicadores = new CalculadoraIndicadores().calcular(ventas);

        System.out.println("===== REPORTE DE VENTAS =====");
        System.out.println("Registros leidos: " + ventas.size());
        System.out.printf("Valor total de ventas: $%,d%n", indicadores.getValorTotal());
        System.out.println("Cantidad total vendida: " + indicadores.getCantidadTotal());
        System.out.printf("Producto con mayor valor vendido: %s ($%,d)%n",
                indicadores.getProductoMayorValor(), indicadores.getValorProductoMayor());
        System.out.printf("Valor promedio por registro: $%,.2f%n", indicadores.getPromedioPorRegistro());
    }
}
