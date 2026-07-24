package com.hackathon.energia_backend.repository;


import com.hackathon.energia_backend.entity.ResultadoAnalisis;

/** Traigo JpaRepository, que es una herramienta de Spring que ya trae hechos los métodos
 * para guardar, buscar, borrar y actualizar en la base de datos
 */
import org.springframework.data.jpa.repository.JpaRepository;

/**Es como ponerle una etiqueta que le dice a Spring:
 * esta clase se encarga de hablar con la base de datos*
 */
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Interfaz de repositorio. Spring Data JPA implementa los métodos CRUD automáticamente.
 * No es necesario escribir SQL para operaciones básicas.
 */

/** le dice a spring esto es un repositorio encargese de crearla y gestionarla automaticamente */
@Repository

/** crea una interface como un contrato y hereda todo de JpaRepository */
public interface ResultadoAnalisisRepository extends JpaRepository<ResultadoAnalisis, Long> {

    /** Spring genera la consulta SQL automáticamente basándose en el nombre del método */
    List<ResultadoAnalisis> findByCategoria(String categoria);
}