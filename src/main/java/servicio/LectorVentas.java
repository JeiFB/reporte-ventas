package servicio;

import modelo.Venta;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Lee el archivo ventas.csv usando la libreria Apache Commons CSV. */
public class LectorVentas {

    public List<Venta> leer(String ruta) throws IOException {
        List<Venta> ventas = new ArrayList<>();

        CSVFormat formato = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setTrim(true)
                .build();

        try (Reader lector = Files.newBufferedReader(Path.of(ruta));
             CSVParser parser = formato.parse(lector)) {
            for (CSVRecord registro : parser) {
                String fecha = registro.get("fecha");
                String producto = registro.get("producto");
                int cantidad = Integer.parseInt(registro.get("cantidad"));
                long valor = Long.parseLong(registro.get("valor"));
                ventas.add(new Venta(fecha, producto, cantidad, valor));
            }
        }
        return ventas;
    }
}
