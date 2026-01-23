package com.inversiones.repository;

import com.inversiones.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Proyecto
 */
@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {
}
