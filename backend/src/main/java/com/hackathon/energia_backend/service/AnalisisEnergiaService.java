package com.hackathon.energia_backend.service;

import com.hackathon.energia_backend.config.AppConfig;
import com.hackathon.energia_backend.dto.request.AnalisisRequest;
import com.hackathon.energia_backend.dto.response.AnalisisHistorialResponse;
import com.hackathon.energia_backend.dto.response.AnalisisResponse;
import com.hackathon.energia_backend.entity.ResultadoAnalisis;
import com.hackathon.energia_backend.entity.Usuario;
import com.hackathon.energia_backend.enums.CategoriaEnergia;
import com.hackathon.energia_backend.exception.AccesoDenegadoException;
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
     * @param req     Objeto DTO con los datos de entrada del análisis.
     * @param usuario Usuario autenticado que realiza el análisis; se persiste
     *                como propietario del registro para alimentar su historial.
     * @return {@link AnalisisResponse} con los resultados del análisis,
     *         incluyendo categoría, costo estimado y recomendaciones.
     */
    // ============================================
    // Método Principal: Realizar Análisis Energético
    // Flujo:
    // 1. Determina la categoría de eficiencia energética
    // 2. Calcula el costo estimado mensual
    // 3. Persiste el resultado en la base de datos
    //    asociándolo al usuario autenticado
    // 4. Construye y retorna la respuesta al cliente
    // ============================================
    public AnalisisResponse realizarAnalisis(AnalisisRequest req, Usuario usuario) {
        // Determinar la categoría de eficiencia basada en las reglas de negocio
        CategoriaEnergia categoria = determinarCategoria(req);

        // Calcular costo estimado aplicando la tarifa configurada
        double costo = req.getConsumoKwh() * AppConfig.TARIFA_KWH;

        // ============================================
        // Paso 1: Persistir en Base de Datos
        // Construye la entidad desde el DTO de request,
        // asociándola al usuario autenticado,
        // y la guarda utilizando el repositorio JPA
        // ============================================
        ResultadoAnalisis guardado = repository.save(ResultadoAnalisis.builder()
                .consumoKwh(req.getConsumoKwh())
                .usoHorarioPico(req.getUsoHorarioPico())
                .cantidadEquipos(req.getCantidadEquipos())
                .tipoInmueble(req.getTipoInmueble())
                .numeroHabitantes(req.getNumeroHabitantes())
                .antiguedadInmueble(req.getAntiguedadInmueble())
                .calefaccion(req.getCalefaccion())
                .aireAcondicionado(req.getAireAcondicionado())
                .horasAltoConsumo(req.getHorasAltoConsumo())
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .costoEstimadoMensual(costo)
                .usuario(usuario)
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
     * Consulta un análisis previamente guardado por su identificador único,
     * verificando que pertenezca al usuario autenticado.
     *
     * @param id      Identificador del análisis a consultar.
     * @param usuario Usuario autenticado que realiza la consulta.
     * @return {@link AnalisisHistorialResponse} con los datos del análisis encontrado.
     * @throws ResourceNotFoundException si no existe un análisis con el ID proporcionado.
     * @throws AccesoDenegadoException   si el análisis pertenece a otro usuario.
     */
    // ============================================
    // Método: Consultar Análisis por ID
    // Recupera un registro existente de la BD verificando
    // que el usuario autenticado sea su propietario
    // ============================================
    public AnalisisHistorialResponse consultarPorId(Long id, Usuario usuario) {
        // Buscar en BD o lanzar excepción si no existe
        ResultadoAnalisis entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + id));

        // Validar propiedad: los análisis huérfanos (sin dueño, creados antes
        // de existir la relación) se tratan como inexistentes para el usuario
        if (entidad.getUsuario() == null || !entidad.getUsuario().getId().equals(usuario.getId())) {
            throw new AccesoDenegadoException("No tienes permiso para consultar este análisis");
        }

        return mapToHistorial(entidad);
    }

    /**
     * Recupera el historial completo de análisis del usuario autenticado,
     * ordenado del más reciente al más antiguo.
     *
     * @param usuario Usuario autenticado cuyo historial se consulta.
     * @return Lista de {@link AnalisisHistorialResponse} con los análisis del usuario.
     */
    // ============================================
    // Método: Listar Historial por Usuario
    // Consulta derivada del repositorio filtrada por
    // el id del usuario autenticado
    // ============================================
    public List<AnalisisHistorialResponse> listarHistorial(Usuario usuario) {
        return repository.findByUsuarioIdOrderByFechaCreacionDesc(usuario.getId())
                .stream()
                .map(this::mapToHistorial)
                .toList();
    }

    /**
     * Transforma una entidad persistida en el DTO de historial,
     * regenerando las recomendaciones a partir de los datos de entrada guardados
     * para que coincidan con las mostradas al momento del análisis.
     *
     * @param entidad Entidad {@link ResultadoAnalisis} persistida.
     * @return {@link AnalisisHistorialResponse} con entrada, resultado y fecha.
     */
    private AnalisisHistorialResponse mapToHistorial(ResultadoAnalisis entidad) {
        CategoriaEnergia categoria = CategoriaEnergia.valueOf(entidad.getCategoria().toUpperCase());

        return AnalisisHistorialResponse.builder()
                .idAnalisis(entidad.getId())
                .consumoKwh(entidad.getConsumoKwh())
                .usoHorarioPico(entidad.getUsoHorarioPico())
                .cantidadEquipos(entidad.getCantidadEquipos())
                .tipoInmueble(entidad.getTipoInmueble())
                .numeroHabitantes(entidad.getNumeroHabitantes())
                .antiguedadInmueble(entidad.getAntiguedadInmueble())
                .calefaccion(entidad.getCalefaccion())
                .aireAcondicionado(entidad.getAireAcondicionado())
                .horasAltoConsumo(entidad.getHorasAltoConsumo())
                .categoria(entidad.getCategoria())
                .probabilidad(entidad.getProbabilidad())
                .recomendaciones(generarRecomendaciones(categoria, toRequest(entidad)))
                .costoEstimadoMensual(entidad.getCostoEstimadoMensual())
                .fechaCreacion(entidad.getFechaCreacion())
                .build();
    }

    /**
     * Reconstruye un {@link AnalisisRequest} desde una entidad persistida,
     * para reutilizar la generación de recomendaciones con los datos originales.
     */
    private AnalisisRequest toRequest(ResultadoAnalisis entidad) {
        AnalisisRequest req = new AnalisisRequest();
        req.setConsumoKwh(entidad.getConsumoKwh());
        req.setUsoHorarioPico(entidad.getUsoHorarioPico());
        req.setCantidadEquipos(entidad.getCantidadEquipos());
        req.setTipoInmueble(entidad.getTipoInmueble());
        req.setNumeroHabitantes(entidad.getNumeroHabitantes());
        req.setAntiguedadInmueble(entidad.getAntiguedadInmueble());
        req.setCalefaccion(entidad.getCalefaccion());
        req.setAireAcondicionado(entidad.getAireAcondicionado());
        req.setHorasAltoConsumo(entidad.getHorasAltoConsumo());
        return req;
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