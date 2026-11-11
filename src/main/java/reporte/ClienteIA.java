package reporte;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Consulta la API de DeepSeek para redactar el reporte ejecutivo.
 * La clave se lee de la variable de entorno DEEPSEEK_API_KEY: nunca se
 * escribe en el codigo ni se guarda en Git.
 */
public class ClienteIA {

    private static final String URL = "https://api.deepseek.com/chat/completions";
    private static final String MODELO = "deepseek-chat";
    private static final String VARIABLE_KEY = "DEEPSEEK_API_KEY";

    private final ObjectMapper mapper = new ObjectMapper();

    public boolean hayApiKey() {
        String key = System.getenv(VARIABLE_KEY);
        return key != null && !key.isBlank();
    }

    public String generarReporte(String prompt) throws IOException, InterruptedException {
        String apiKey = System.getenv(VARIABLE_KEY);
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("No se encontro la variable de entorno " + VARIABLE_KEY);
        }

        ObjectNode cuerpo = mapper.createObjectNode();
        cuerpo.put("model", MODELO);
        ArrayNode mensajes = cuerpo.putArray("messages");
        ObjectNode mensaje = mensajes.addObject();
        mensaje.put("role", "user");
        mensaje.put("content", prompt);

        HttpRequest peticion = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(cuerpo)))
                .build();

        HttpResponse<String> respuesta = HttpClient.newHttpClient()
                .send(peticion, HttpResponse.BodyHandlers.ofString());

        if (respuesta.statusCode() != 200) {
            throw new IOException("La API respondio con codigo " + respuesta.statusCode() + ": " + respuesta.body());
        }

        JsonNode raiz = mapper.readTree(respuesta.body());
        return raiz.path("choices").get(0).path("message").path("content").asText().trim();
    }
}
