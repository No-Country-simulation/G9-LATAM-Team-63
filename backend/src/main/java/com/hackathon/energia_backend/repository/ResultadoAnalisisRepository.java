package com.hackathon.energia_backend.repository;

import com.hackathon.energia_backend.entity.ResultadoAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interfaz de repositorio para la entidad {@link ResultadoAnalisis}.
 *
 * Extiende de {@link JpaRepository} para aprovechar las capacidades de
 * Spring Data JPA, las cuales proporcionan:
 * <ul>
 *   <li><b>Implementación automática de CRUD:</b> Operaciones básicas (save, findById, findAll, delete)
 *       sin necesidad de escribir código repetitivo ni SQL manual.</li>
 *   <li><b>Patrón Repositorio:</b> Abstrae la capa de persistencia, desacoplando la lógica de negocio
 *       del acceso directo a la base de datos.</li>
 *   <li><b>Traducción de excepciones:</b> Convierte las excepciones nativas de la base de datos
 *       en la jerarquía unificada de Spring ({@link org.springframework.dao.DataAccessException}).</li>
 * </ul>
  */
@Repository
public interface ResultadoAnalisisRepository extends JpaRepository<ResultadoAnalisis, Long> {

    // ============================================
    // Consultas Derivadas (Query Derivation)
    // Spring Data JPA genera la consulta SQL dinámicamente
    // en tiempo de ejecución, analizando la firma del método
    // (findBy + NombreDelCampo).
    // ============================================

    /**
     * Busca todos los registros de análisis que coincidan con una categoría específica.
     *
     * @param categoria El nombre de la categoría a filtrar (ej. "Eficiente", "Moderado").
     * @return Una lista de {@link ResultadoAnalisis} que cumplen con el criterio de búsqueda.
     */
    List<ResultadoAnalisis> findByCategoria(String categoria);
}