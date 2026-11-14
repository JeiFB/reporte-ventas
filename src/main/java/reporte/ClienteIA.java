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
 * Consulta la API de OpenRouter (compatible con OpenAI) para redactar el
 * reporte ejecutivo con un modelo gratis. Si un modelo esta ocupado o no
 * disponible, prueba con el siguiente de la lista. La clave se lee de la
 * variable de entorno OPENROUTER_API_KEY: nunca se escribe en el codigo.
 */
public class ClienteIA {

    private static final String URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String VARIABLE_KEY = "OPENROUTER_API_KEY";
    private static final String VARIABLE_MODELO = "OPENROUTER_MODEL";

    private static final String[] MODELOS_GRATIS = {
            "google/gemma-4-26b-a4b-it:free",
            "nvidia/nemotron-3.5-super-vl:free",
            "deepseek/deepseek-chat-v3-0324:free",
            "nvidia/nemotron-3-ultra-550b-a55b:free"
    };

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

        String[] modelos = modelosAUsar();
        String ultimoError = "";

        for (String modelo : modelos) {
            HttpResponse<String> respuesta = enviar(apiKey, modelo, prompt);
            if (respuesta.statusCode() == 200) {
                System.out.println("Modelo usado: " + modelo);
                JsonNode raiz = mapper.readTree(respuesta.body());
                return raiz.path("choices").get(0).path("message").path("content").asText().trim();
            }
            ultimoError = "modelo " + modelo + " respondio " + respuesta.statusCode();
        }

        throw new IOException("Ningun modelo gratis respondio. " + ultimoError);
    }

    private String[] modelosAUsar() {
        String override = System.getenv(VARIABLE_MODELO);
        if (override != null && !override.isBlank()) {
            return new String[]{override};
        }
        return MODELOS_GRATIS;
    }

    private HttpResponse<String> enviar(String apiKey, String modelo, String prompt)
            throws IOException, InterruptedException {
        ObjectNode cuerpo = mapper.createObjectNode();
        cuerpo.put("model", modelo);
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

        return HttpClient.newHttpClient().send(peticion, HttpResponse.BodyHandlers.ofString());
    }
}
