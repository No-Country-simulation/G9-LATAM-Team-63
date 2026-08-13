package com.hackathon.energia_backend.client;


import com.hackathon.energia_backend.dto.datascience.DataScienceRequest;
import com.hackathon.energia_backend.dto.datascience.DataScienceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class DataScienceClient {

    // ==========================================
    // Dependencias y configuración de endpoint
    // Cliente HTTP y URL completa del servicio ML
    // ==========================================
    private final RestTemplate restTemplate;
    private final String predictUrl;

    // ==========================================
    // Inyección de dependencias y construcción de URL
    // Ensambla la ruta completa a partir de propiedades externas
    // ==========================================
    public DataScienceClient(
            RestTemplate restTemplate,
            @Value("${app.datascience.base-url:http://localhost:8000}") String baseUrl,
            @Value("${app.datascience.predict-path:/api/v1/predict/}") String predictPath) {
        this.restTemplate = restTemplate;
        this.predictUrl = baseUrl + predictPath;
    }

    // ==========================================
    // Ejecución de predicción contra API Data Science
    // Realiza la llamada POST y gestiona la respuesta
    // ==========================================
    public DataScienceResponse predict(DataScienceRequest request) {
        log.info("Llamando a API Data Science: {}", predictUrl);

        // ==========================================
        // Preparación de la petición HTTP
        // Configura headers JSON y empaqueta el body
        // ==========================================
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DataScienceRequest> entity = new HttpEntity<>(request, headers);

        try {
            // ==========================================
            // Consumo del servicio externo
            // Envía la solicitud POST y recibe la predicción
            // ==========================================
            ResponseEntity<DataScienceResponse> response = restTemplate.exchange(
                    predictUrl,
                    HttpMethod.POST,
                    entity,
                    DataScienceResponse.class
            );

            // ==========================================
            // Procesamiento de respuesta exitosa
            // Extrae el body y registra los resultados obtenidos
            // ==========================================
            DataScienceResponse body = response.getBody();
            log.info("Respuesta DS: categoria={}, probabilidad={}",
                    body != null ? body.getCategoria() : "null",
                    body != null ? body.getProbabilidad() : "null");
            return body;

        } catch (HttpClientErrorException e) {
            log.error("Error 4xx en API DS: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("El servicio de predicción rechazó la solicitud: " + e.getStatusCode());
        } catch (ResourceAccessException e) {
            log.error("API DS no disponible: {}", e.getMessage());
            throw new RuntimeException("El servicio de predicción no está disponible");
        } catch (Exception e) {
            log.error("Error inesperado en API DS: {}", e.getMessage());
            throw new RuntimeException("Error al comunicarse con el servicio de predicción");
        }
    }
}