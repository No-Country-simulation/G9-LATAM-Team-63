package com.hackathon.energia_backend.service;

// Importaciones de configuración, DTOs (objetos de transferencia de datos), entidad, enums, excepciones y repositorio del proyecto
import com.hackathon.energia_backend.config.AppConfig;
import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.entity.ResultadoAnalisis;
import com.hackathon.energia_backend.enums.CategoriaEnergia;
import com.hackathon.energia_backend.exception.ResourceNotFoundException;
import com.hackathon.energia_backend.repository.ResultadoAnalisisRepository;

// Lombok para generar automáticamente el constructor con los campos 'final'
import lombok.RequiredArgsConstructor;

// Anotación de Spring que marca esta clase como un componente de capa de servicio (lógica de negocio)
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor // Genera un constructor que inyecta automáticamente las dependencias 'final' (como el repository)
public class AnalisisEnergiaService {

    // Inyección de dependencia del repositorio para interactuar con la base de datos (operaciones CRUD)
    private final ResultadoAnalisisRepository repository;

    /**
     * Método principal que realiza el análisis de consumo energético.
     * Recibe una solicitud (Request), procesa los datos, guarda el resultado en la base de datos
     * y devuelve una respuesta (Response) con el análisis completo.
     *
     * @param request Datos de entrada del usuario (consumo, horarios, equipos, tipo de inmueble).
     * @return AnalisisResponse con los resultados del análisis y recomendaciones.
     */
    public AnalisisResponse realizarAnalisis(AnalisisRequest request) {

        // 1. Calcular el costo estimado multiplicando el consumo en kWh por la tarifa definida en la configuración
        double costo = request.getConsumoKwh() * AppConfig.TARIFA_KWH;

        // 2. Determinar la categoría de eficiencia energética (EFICIENTE, MODERADO, INEFICIENTE) según las reglas de negocio
        CategoriaEnergia categoria = determinarCategoria(request);

        // 3. Generar una lista de recomendaciones personalizadas basadas en la categoría y los datos de la solicitud
        List<String> recomendaciones = generarRecomendaciones(categoria, request);

        // 4. Crear la entidad de JPA usando el patrón Builder y guardarla en la base de datos
        ResultadoAnalisis entidad = ResultadoAnalisis.builder()
                .consumoKwh(request.getConsumoKwh())
                .usoHorarioPico(request.getUsoHorarioPico())
                .cantidadEquipos(request.getCantidadEquipos())
                .tipoInmueble(request.getTipoInmueble())
                .categoria(categoria.getNombre()) // Guarda el nombre del enum como String en la BD
                .probabilidad(categoria.getProbabilidadBase())
                .costoEstimadoMensual(costo)
                .build();

        // Guarda la entidad en la base de datos y obtiene el registro con su ID generado
        ResultadoAnalisis guardado = repository.save(entidad);

        // 5. Construir y retornar la respuesta (DTO) al cliente, incluyendo el ID del registro recién guardado
        return AnalisisResponse.builder()
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .recomendaciones(recomendaciones)
                .costoEstimadoMensual(costo)
                .idAnalisis(guardado.getId())
                .build();
    }

    /**
     * Consulta un análisis previamente guardado en la base de datos por su ID.
     *
     * @param id Identificador único del análisis.
     * @return AnalisisResponse con los datos del análisis y recomendaciones recalculadas.
     * @throws ResourceNotFoundException si no se encuentra ningún registro con el ID proporcionado.
     */
    public AnalisisResponse consultarPorId(Long id) {

        // Busca el registro en la BD. Si no existe, lanza una excepción personalizada de recurso no encontrado
        ResultadoAnalisis entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Análisis no encontrado con ID: " + id));

        // Convierte el nombre de la categoría almacenado en String de vuelta a su tipo Enum para poder usarlo
        CategoriaEnergia categoria = CategoriaEnergia.valueOf(entidad.getCategoria().toUpperCase());

        // Construye y retorna la respuesta.
        // Nota: Se pasa 'null' como request en las recomendaciones porque no tenemos el objeto original,
        // pero el método 'generarRecomendaciones' está diseñado para manejar ese caso de forma segura.
        return AnalisisResponse.builder()
                .categoria(entidad.getCategoria())
                .probabilidad(entidad.getProbabilidad())
                .recomendaciones(generarRecomendaciones(categoria, null))
                .costoEstimadoMensual(entidad.getCostoEstimadoMensual())
                .idAnalisis(entidad.getId())
                .build();
    }

    /**
     * Método privado auxiliar que aplica las reglas de negocio para clasificar el consumo energético.
     *
     * @param request Datos de la solicitud del usuario.
     * @return La categoría de energía correspondiente (Enum).
     */
    private CategoriaEnergia determinarCategoria(AnalisisRequest request) {
        // Si el consumo es alto (>400 kWh) Y además se usa en horario pico, se considera INEFICIENTE
        if (request.getConsumoKwh() > 400 && request.getUsoHorarioPico()) {
            return CategoriaEnergia.INEFICIENTE;
        }
        // Si el consumo es bajo (<200 kWh) Y NO se usa en horario pico, se considera EFICIENTE
        else if (request.getConsumoKwh() < 200 && !request.getUsoHorarioPico()) {
            return CategoriaEnergia.EFICIENTE;
        }
        // Cualquier otro caso intermedio se clasifica como MODERADO
        return CategoriaEnergia.MODERADO;
    }

    /**
     * Método privado auxiliar que genera una lista de recomendaciones de ahorro energético.
     *
     * @param categoria La categoría de eficiencia ya determinada.
     * @param request   Los datos originales de la solicitud (puede ser null si se consulta desde la BD).
     * @return Lista de strings con las recomendaciones aplicables.
     */
    private List<String> generarRecomendaciones(CategoriaEnergia categoria, AnalisisRequest request) {
        List<String> recomendaciones = new ArrayList<>();

        // Selecciona las recomendaciones base según la categoría evaluada usando un switch moderno de Java
        switch (categoria) {
            case INEFICIENTE -> {
                recomendaciones.add("Reducir el uso de equipos durante los horarios pico");
                recomendaciones.add("Evaluar equipos con alto consumo energético");
            }
            case EFICIENTE -> {
                recomendaciones.add("Mantener las prácticas actuales de consumo");
                recomendaciones.add("Monitorear periódicamente el consumo");
            }
            case MODERADO -> recomendaciones.add("Identificar oportunidades de ahorro en horarios pico");
        }

        // Regla de negocio adicional: si tenemos el request y la cantidad de equipos es mayor a 10,
        // se agrega una recomendación extra independientemente de la categoría.
        // La validación 'request != null' evita un NullPointerException cuando se llama desde consultarPorId.
        if (request != null && request.getCantidadEquipos() > 10) {
            recomendaciones.add("Considerar reemplazo de equipos antiguos por modelos eficientes");
        }

        return recomendaciones;
    }
}