package com.inversiones.repository;

import com.inversiones.entity.TipoRendimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para la entidad TipoRendimiento
 */
@Repository
public interface TipoRendimientoRepository extends JpaRepository<TipoRendimiento, Integer> {
}
