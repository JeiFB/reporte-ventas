package reporte;

import modelo.Indicadores;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Construye un resumen estructurado de los indicadores y el prompt para que
 * una IA redacte el reporte ejecutivo. Java hace los calculos; la IA solo
 * interpreta y redacta.
 */
public class GeneradorResumenIA {

    public String construirResumen(Indicadores indicadores) {
        return String.format(Locale.US,
                "RESUMEN DE INDICADORES DE VENTAS%n"
                        + "- Valor total de ventas: $%,d COP%n"
                        + "- Cantidad total vendida: %d unidades%n"
                        + "- Producto con mayor valor vendido: %s ($%,d COP)%n"
                        + "- Valor promedio por registro: $%,.2f COP",
                indicadores.getValorTotal(),
                indicadores.getCantidadTotal(),
                indicadores.getProductoMayorValor(),
                indicadores.getValorProductoMayor(),
                indicadores.getPromedioPorRegistro());
    }

    public String construirPrompt(Indicadores indicadores) {
        return "Actua como analista de negocios. Con base en los siguientes indicadores "
                + "ya calculados, redacta un reporte ejecutivo claro de maximo tres parrafos "
                + "para la gerencia. No inventes cifras ni recalcules nada: usa solo estos datos. "
                + "Explica el desempeno general, destaca el producto mas importante y sugiere una "
                + "recomendacion breve.%n%n".formatted()
                + construirResumen(indicadores);
    }

    public void guardarPrompt(String prompt, String ruta) throws IOException {
        Files.writeString(Path.of(ruta), prompt);
    }
}
