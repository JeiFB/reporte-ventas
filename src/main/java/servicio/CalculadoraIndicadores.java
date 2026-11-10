package servicio;

import modelo.Indicadores;
import modelo.Venta;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Calcula los indicadores de ventas a partir de la lista de registros. */
public class CalculadoraIndicadores {

    public Indicadores calcular(List<Venta> ventas) {
        long valorTotal = ventas.stream().mapToLong(Venta::getValor).sum();
        int cantidadTotal = ventas.stream().mapToInt(Venta::getCantidad).sum();

        Map<String, Long> porProducto = ventasPorProducto(ventas);
        Map.Entry<String, Long> mayor = porProducto.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        double promedio = ventas.isEmpty() ? 0 : (double) valorTotal / ventas.size();

        return new Indicadores(valorTotal, cantidadTotal, mayor.getKey(), mayor.getValue(), promedio);
    }

    /** Suma el valor vendido por cada producto (usado tambien por la grafica). */
    public Map<String, Long> ventasPorProducto(List<Venta> ventas) {
        return ventas.stream().collect(Collectors.groupingBy(
                Venta::getProducto, TreeMap::new, Collectors.summingLong(Venta::getValor)));
    }
}
