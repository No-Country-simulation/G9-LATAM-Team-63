package com.hackathon.energia_backend.config;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataScienceConfig {

    // ==========================================
    // Configuración del cliente HTTP RestTemplate
    // Define timeouts de conexión y lectura desde application.properties
    // ==========================================
    @Bean
    public RestTemplate restTemplate(
            @Value("${app.datascience.connect-timeout-ms:5000}") int connectTimeout,
            @Value("${app.datascience.read-timeout-ms:10000}") int readTimeout) {

        // ==========================================
        // Factory con timeouts personalizados
        // Aplica los valores configurados al cliente HTTP
        // ==========================================
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeout);
        return new RestTemplate(factory);
    }
}