package com.inversiones.repository;

import com.inversiones.entity.Rendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad Rendimiento
 */
@Repository
public interface RendimientoRepository extends JpaRepository<Rendimiento, Integer> {
}
