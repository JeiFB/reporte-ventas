import modelo.Indicadores;
import modelo.Venta;
import reporte.ClienteIA;
import reporte.GeneradorGrafica;
import reporte.GeneradorResumenIA;
import servicio.CalculadoraIndicadores;
import servicio.LectorVentas;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String RUTA_CSV = "datos/ventas.csv";
    private static final String CARPETA_SALIDA = "salida";

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Path.of(CARPETA_SALIDA));

        List<Venta> ventas = new LectorVentas().leer(RUTA_CSV);
        CalculadoraIndicadores calculadora = new CalculadoraIndicadores();
        Indicadores indicadores = calculadora.calcular(ventas);

        System.out.println("===== REPORTE DE VENTAS =====");
        System.out.println("Registros leidos: " + ventas.size());
        System.out.printf("Valor total de ventas: $%,d%n", indicadores.getValorTotal());
        System.out.println("Cantidad total vendida: " + indicadores.getCantidadTotal());
        System.out.printf("Producto con mayor valor vendido: %s ($%,d)%n",
                indicadores.getProductoMayorValor(), indicadores.getValorProductoMayor());
        System.out.printf("Valor promedio por registro: $%,.2f%n", indicadores.getPromedioPorRegistro());

        Map<String, Long> ventasPorProducto = calculadora.ventasPorProducto(ventas);
        String rutaGrafica = new GeneradorGrafica()
                .generar(ventasPorProducto, CARPETA_SALIDA + "/ventas_por_producto");
        System.out.println();
        System.out.println("Grafica generada en: " + rutaGrafica);

        GeneradorResumenIA generadorIA = new GeneradorResumenIA();
        String prompt = generadorIA.construirPrompt(indicadores);
        String rutaPrompt = CARPETA_SALIDA + "/prompt_ia.txt";
        generadorIA.guardarPrompt(prompt, rutaPrompt);
        System.out.println("Prompt para la IA generado en: " + rutaPrompt);

        System.out.println();
        System.out.println("----- Resumen estructurado para la IA -----");
        System.out.println(generadorIA.construirResumen(indicadores));

        ClienteIA clienteIA = new ClienteIA();
        String rutaRespuesta = CARPETA_SALIDA + "/respuesta_ia.txt";
        if (clienteIA.hayApiKey()) {
            try {
                String reporte = clienteIA.generarReporte(prompt);
                Files.writeString(Path.of(rutaRespuesta), reporte);
                System.out.println();
                System.out.println("----- Reporte ejecutivo redactado por la IA -----");
                System.out.println(reporte);
                System.out.println();
                System.out.println("(Tambien guardado en: " + rutaRespuesta + ")");
            } catch (Exception e) {
                System.out.println();
                System.out.println("No se pudo consultar la IA: " + e.getMessage());
            }
        } else {
            System.out.println();
            System.out.println("Nota: define la variable de entorno OPENROUTER_API_KEY para generar el");
            System.out.println("reporte automaticamente. Sin ella, usa el prompt de " + rutaPrompt
                    + " en una IA de forma manual.");
        }
    }
}
