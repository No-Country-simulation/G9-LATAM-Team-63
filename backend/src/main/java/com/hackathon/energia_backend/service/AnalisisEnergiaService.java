package com.hackathon.energia_backend.service;

import com.hackathon.energia_backend.config.AppConfig;
import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.entity.ResultadoAnalisis;
import com.hackathon.energia_backend.enums.CategoriaEnergia;
import com.hackathon.energia_backend.exception.ResourceNotFoundException;
import com.hackathon.energia_backend.repository.ResultadoAnalisisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de negocio para el análisis de consumo energético.
 *
 * Esta clase encapsula la lógica de negocio principal de la aplicación,
 * coordinando la validación de datos, el procesamiento del análisis,
 * la persistencia en base de datos y la generación de recomendaciones
 * personalizadas basadas en patrones de consumo.
 */
@Service
@RequiredArgsConstructor
public class AnalisisEnergiaService {

    // ============================================
    // Inyección de Dependencias
    // Repositorio para operaciones de persistencia
    // de la entidad ResultadoAnalisis
    // ============================================
    private final ResultadoAnalisisRepository repository;

    /**
     * Procesa una solicitud de análisis energético completo.
     *
     * @param req Objeto DTO con los datos de entrada del análisis.
     * @return {@link AnalisisResponse} con los resultados del análisis,
     *         incluyendo categoría, costo estimado y recomendaciones.
     */
    // ============================================
    // Método Principal: Realizar Análisis Energético
    // Flujo:
    // 1. Determina la categoría de eficiencia energética
    // 2. Calcula el costo estimado mensual
    // 3. Persiste el resultado en la base de datos
    // 4. Construye y retorna la respuesta al cliente
    // ============================================
    public AnalisisResponse realizarAnalisis(AnalisisRequest req) {
        // Determinar la categoría de eficiencia basada en las reglas de negocio
        CategoriaEnergia categoria = determinarCategoria(req);

        // Calcular costo estimado aplicando la tarifa configurada
        double costo = req.getConsumoKwh() * AppConfig.TARIFA_KWH;

        // ============================================
        // Paso 1: Persistir en Base de Datos
        // Construye la entidad desde el DTO de request
        // y la guarda utilizando el repositorio JPA
        // ============================================
        ResultadoAnalisis guardado = repository.save(ResultadoAnalisis.builder()
                .consumoKwh(req.getConsumoKwh())
                .usoHorarioPico(req.getUsoHorarioPico())
                .cantidadEquipos(req.getCantidadEquipos())
                .tipoInmueble(req.getTipoInmueble())
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .costoEstimadoMensual(costo)
                .build());

        // ============================================
        // Paso 2: Construir Respuesta al Cliente
        // Transforma la entidad guardada en DTO de respuesta
        // incluyendo recomendaciones personalizadas
        // ============================================
        return AnalisisResponse.builder()
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .recomendaciones(generarRecomendaciones(categoria, req))
                .costoEstimadoMensual(costo)
                .idAnalisis(guardado.getId())
                .build();
    }

    /**
     * Consulta un análisis previamente guardado por su identificador único.
     *
     * @param id Identificador del análisis a consultar.
     * @return {@link AnalisisResponse} con los datos del análisis encontrado.
     * @throws ResourceNotFoundException si no existe un análisis con el ID proporcionado.
     */
    // ============================================
    // Método: Consultar Análisis por ID
    // Recupera un registro existente de la BD
    // y lo transforma en DTO de respuesta
    // ============================================
    public AnalisisResponse consultarPorId(Long id) {
        // Buscar en BD o lanzar excepción si no existe
        ResultadoAnalisis entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + id));

        // Convertir el string almacenado a enum para procesamiento
        CategoriaEnergia categoria = CategoriaEnergia.valueOf(entidad.getCategoria().toUpperCase());

        // ============================================
        // Construir Respuesta desde Entidad Persistida
        // solo basándose en la categoría
        // ============================================
        return AnalisisResponse.builder()
                .categoria(entidad.getCategoria())
                .probabilidad(entidad.getProbabilidad())
                .recomendaciones(generarRecomendaciones(categoria, null))
                .costoEstimadoMensual(entidad.getCostoEstimadoMensual())
                .idAnalisis(entidad.getId())
                .build();
    }

    /**
     * Clasifica el consumo energético según las reglas de negocio.
     *
     * @param req Datos de entrada del análisis.
     * @return {@link CategoriaEnergia} que representa la eficiencia del consumo.
     */
    // ============================================
    // Método Privado: Determinar Categoría Energética
    // Aplica las mismas reglas de clasificación
    // Criterios:
    // - Ratio kWh/equipo > 55 y horas > 7 → INEFICIENTE
    // - Ratio kWh/equipo < 28 y horas < 4 → EFICIENTE
    // - Cualquier otro caso → MODERADO
    // ============================================
    private CategoriaEnergia determinarCategoria(AnalisisRequest req) {
        double ratio = req.getConsumoKwh() / req.getCantidadEquipos();
        int horas = req.getHorasAltoConsumo();

        if (ratio > 55 && horas > 7) return CategoriaEnergia.INEFICIENTE;
        if (ratio < 28 && horas < 4) return CategoriaEnergia.EFICIENTE;
        return CategoriaEnergia.MODERADO;
    }

    /**
     * Genera recomendaciones personalizadas basadas en la categoría y patrones de consumo.
     *
     * @param cat Categoría de eficiencia energética determinada.
     * @param req Datos originales del análisis (puede ser null si se consulta desde BD).
     * @return Lista de recomendaciones aplicables al caso específico.
     */
    // ============================================
    // Método Privado: Generar Recomendaciones
    // Construye un conjunto de sugerencias dinámicas
    // combinando:
    // 1. Recomendaciones base según la categoría
    // 2. Reglas adicionales transversales según
    //    tipo de inmueble y cantidad de equipos
    // ============================================
    private List<String> generarRecomendaciones(CategoriaEnergia cat, AnalisisRequest req) {
        List<String> recs = new ArrayList<>();

        // ============================================
        // Regla 1: Recomendaciones Base por Categoría
        // Utiliza switch moderno de Java
        // para mayor legibilidad y mantenibilidad
        // ============================================
        switch (cat) {
            case INEFICIENTE -> {
                recs.add("Reducir las horas de alto consumo (actualmente > 7h)");
                recs.add("Evaluar equipos con alto consumo por unidad (ratio kWh/equipo alto)");
                if (req != null && req.getUsoHorarioPico()) recs.add("Desplazar el consumo a horarios no pico");
            }
            case EFICIENTE -> {
                recs.add("Mantener las prácticas actuales de consumo");
                recs.add("Monitorear periódicamente el consumo para asegurar la eficiencia");
            }
            case MODERADO -> {
                recs.add("Identificar oportunidades de ahorro en horarios pico");
                if (req != null && (req.getConsumoKwh() / req.getCantidadEquipos()) > 40) {
                    recs.add("Revisar equipos individuales con mayor consumo para optimizar el ratio");
                }
            }
        }

        // ============================================
        // Regla 2: Recomendaciones Transversales
        // Se aplican independientemente de la categoría
        // ============================================
        if (req != null) {
            if ("Oficina".equalsIgnoreCase(req.getTipoInmueble()))
                recs.add("Implementar apagado automático de equipos al cierre de jornada");
            if (req.getCantidadEquipos() > 10)
                recs.add("Considerar el reemplazo de equipos antiguos por modelos con certificación energética");
        }

        return recs;
    }
}