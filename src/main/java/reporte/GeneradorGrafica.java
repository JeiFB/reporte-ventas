package reporte;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Genera la grafica de ventas por producto con la libreria XChart. */
public class GeneradorGrafica {

    public String generar(Map<String, Long> ventasPorProducto, String rutaSinExtension) throws IOException {
        CategoryChart grafica = new CategoryChartBuilder()
                .width(900).height(600)
                .title("Ventas por producto")
                .xAxisTitle("Producto")
                .yAxisTitle("Valor vendido (COP)")
                .build();

        grafica.getStyler().setLegendVisible(false);
        grafica.getStyler().setPlotGridVerticalLinesVisible(false);

        List<String> productos = new ArrayList<>(ventasPorProducto.keySet());
        List<Long> valores = new ArrayList<>(ventasPorProducto.values());
        grafica.addSeries("Valor vendido", productos, valores);

        BitmapEncoder.saveBitmap(grafica, rutaSinExtension, BitmapEncoder.BitmapFormat.PNG);
        return rutaSinExtension + ".png";
    }
}
