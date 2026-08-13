package com.hackathon.energia_backend.service;


import com.hackathon.energia_backend.client.DataScienceClient;
import com.hackathon.energia_backend.config.AppConfig;
import com.hackathon.energia_backend.dto.datascience.DataScienceRequest;
import com.hackathon.energia_backend.dto.datascience.DataScienceResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalisisEnergiaService {

    private final ResultadoAnalisisRepository repository;
    private final DataScienceClient dataScienceClient;

    // ============================================
    // Método Principal: Realizar Análisis
    // 1. Intenta usar la API de Data Science (XGBoost)
    // 2. Si falla, hace fallback al motor de reglas local
    // ============================================
    public AnalisisResponse realizarAnalisis(AnalisisRequest req, Usuario usuario) {
        try {
            return realizarAnalisisConDataScience(req, usuario);
        } catch (Exception e) {
            log.warn("Fallback a motor local. Razón: {}", e.getMessage());
            return realizarAnalisisLocal(req, usuario);
        }
    }

    // ============================================
    // Integración con API Data Science
    // ============================================
    private AnalisisResponse realizarAnalisisConDataScience(AnalisisRequest req, Usuario usuario) {
        DataScienceRequest dsReq = mapToDataScienceRequest(req);
        DataScienceResponse dsResp = dataScienceClient.predict(dsReq);

        double costo = dsResp.getCostoEstimadoMensual() != null
                ? dsResp.getCostoEstimadoMensual()
                : req.getConsumoKwh() * AppConfig.TARIFA_KWH;

        double probabilidad = dsResp.getProbabilidad() != null ? dsResp.getProbabilidad() : 0.0;

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
                .categoria(dsResp.getCategoria())
                .probabilidad(probabilidad)
                .costoEstimadoMensual(costo)
                .usuario(usuario)
                .build());

        return AnalisisResponse.builder()
                .categoria(dsResp.getCategoria())
                .probabilidad(probabilidad)
                .recomendaciones(dsResp.getRecomendaciones())
                .costoEstimadoMensual(costo)
                .idAnalisis(guardado.getId())
                .build();
    }

    private DataScienceRequest mapToDataScienceRequest(AnalisisRequest req) {
        DataScienceRequest ds = new DataScienceRequest();
        ds.setConsumoKwh(req.getConsumoKwh());
        ds.setCantidadEquipos(req.getCantidadEquipos());
        ds.setHorasAltoConsumo(req.getHorasAltoConsumo());
        ds.setTipoInmueble(req.getTipoInmueble());
        ds.setUsoHorarioPico(req.getUsoHorarioPico());
        ds.setNumeroHabitantes(req.getNumeroHabitantes());
        ds.setAntiguedadInmueble(req.getAntiguedadInmueble());
        ds.setCalefaccion(req.getCalefaccion());
        ds.setAireAcondicionado(req.getAireAcondicionado());
        return ds;
    }

    // ============================================
    // Fallback: Motor de reglas local (original)
    // ============================================
    private AnalisisResponse realizarAnalisisLocal(AnalisisRequest req, Usuario usuario) {
        CategoriaEnergia categoria = determinarCategoria(req);
        double costo = req.getConsumoKwh() * AppConfig.TARIFA_KWH;

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

        return AnalisisResponse.builder()
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .recomendaciones(generarRecomendaciones(categoria, req))
                .costoEstimadoMensual(costo)
                .idAnalisis(guardado.getId())
                .build();
    }

    // ============================================
    // Consultas (sin cambios funcionales)
    // ============================================
    public AnalisisHistorialResponse consultarPorId(Long id, Usuario usuario) {
        ResultadoAnalisis entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + id));

        if (entidad.getUsuario() == null || !entidad.getUsuario().getId().equals(usuario.getId())) {
            throw new AccesoDenegadoException("No tienes permiso para consultar este análisis");
        }

        return mapToHistorial(entidad);
    }

    public List<AnalisisHistorialResponse> listarHistorial(Usuario usuario) {
        return repository.findByUsuarioIdOrderByFechaCreacionDesc(usuario.getId())
                .stream()
                .map(this::mapToHistorial)
                .toList();
    }

    // ============================================
    // Mapeos y utilidades (sin cambios)
    // ============================================
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

    private CategoriaEnergia determinarCategoria(AnalisisRequest req) {
        double ratio = req.getConsumoKwh() / req.getCantidadEquipos();
        int horas = req.getHorasAltoConsumo();

        if (ratio > 55 && horas > 7) return CategoriaEnergia.INEFICIENTE;
        if (ratio < 28 && horas < 4) return CategoriaEnergia.EFICIENTE;
        return CategoriaEnergia.MODERADO;
    }

    private List<String> generarRecomendaciones(CategoriaEnergia cat, AnalisisRequest req) {
        List<String> recs = new ArrayList<>();

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

        if (req != null) {
            if ("Oficina".equalsIgnoreCase(req.getTipoInmueble()))
                recs.add("Implementar apagado automático de equipos al cierre de jornada");
            if (req.getCantidadEquipos() > 10)
                recs.add("Considerar el reemplazo de equipos antiguos por modelos con certificación energética");
        }

        return recs;
    }
}