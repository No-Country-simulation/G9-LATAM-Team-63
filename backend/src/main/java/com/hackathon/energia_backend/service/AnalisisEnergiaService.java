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

@Service
@RequiredArgsConstructor
public class AnalisisEnergiaService {

    private final ResultadoAnalisisRepository repository;

    /**
     * Procesa la solicitud, guarda en BD y retorna la respuesta con el análisis.
     */
    public AnalisisResponse realizarAnalisis(AnalisisRequest req) {
        CategoriaEnergia categoria = determinarCategoria(req);
        double costo = req.getConsumoKwh() * AppConfig.TARIFA_KWH;

        // 1. Guardar en base de datos
        ResultadoAnalisis guardado = repository.save(ResultadoAnalisis.builder()
                .consumoKwh(req.getConsumoKwh())
                .usoHorarioPico(req.getUsoHorarioPico())
                .cantidadEquipos(req.getCantidadEquipos())
                .tipoInmueble(req.getTipoInmueble())
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .costoEstimadoMensual(costo)
                .build());

        // 2. Construir y retornar la respuesta al cliente
        return AnalisisResponse.builder()
                .categoria(categoria.getNombre())
                .probabilidad(categoria.getProbabilidadBase())
                .recomendaciones(generarRecomendaciones(categoria, req))
                .costoEstimadoMensual(costo)
                .idAnalisis(guardado.getId())
                .build();
    }

    /**
     * Consulta un análisis guardado por su ID.
     */
    public AnalisisResponse consultarPorId(Long id) {
        ResultadoAnalisis entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + id));

        CategoriaEnergia categoria = CategoriaEnergia.valueOf(entidad.getCategoria().toUpperCase());

        return AnalisisResponse.builder()
                .categoria(entidad.getCategoria())
                .probabilidad(entidad.getProbabilidad())
                .recomendaciones(generarRecomendaciones(categoria, null)) // null porque no hay request original
                .costoEstimadoMensual(entidad.getCostoEstimadoMensual())
                .idAnalisis(entidad.getId())
                .build();
    }

    /**
     * Clasifica el consumo replicando la lógica exacta del script de Python (generador_datos.py).
     */
    private CategoriaEnergia determinarCategoria(AnalisisRequest req) {
        double ratio = req.getConsumoKwh() / req.getCantidadEquipos();
        int horas = req.getHorasAltoConsumo();

        if (ratio > 55 && horas > 7) return CategoriaEnergia.INEFICIENTE;
        if (ratio < 28 && horas < 4) return CategoriaEnergia.EFICIENTE;
        return CategoriaEnergia.MODERADO;
    }

    /**
     * Genera recomendaciones dinámicas basadas en la categoría y las variables del dataset.
     */
    private List<String> generarRecomendaciones(CategoriaEnergia cat, AnalisisRequest req) {
        List<String> recs = new ArrayList<>();

        // 1. Recomendaciones base según la categoría (usando switch moderno de Java)
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

        // 2. Reglas adicionales transversales (solo si tenemos el request original)
        if (req != null) {
            if ("Oficina".equalsIgnoreCase(req.getTipoInmueble()))
                recs.add("Implementar apagado automático de equipos al cierre de jornada");
            if (req.getCantidadEquipos() > 10)
                recs.add("Considerar el reemplazo de equipos antiguos por modelos con certificación energética");
        }

        return recs;
    }
}