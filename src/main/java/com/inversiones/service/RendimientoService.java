package com.inversiones.service;

import com.inversiones.entity.Rendimiento;
import com.inversiones.repository.RendimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para las operaciones CRUD de Rendimiento
 */
@Service
public class RendimientoService {

    @Autowired
    private RendimientoRepository repository;

    public List<Rendimiento> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Rendimiento> obtenerPorId(Integer id) {
        return repository.findById(id);
    }

    public Rendimiento guardar(Rendimiento rendimiento) {
        return repository.save(rendimiento);
    }

    public Rendimiento actualizar(Integer id, Rendimiento rendimiento) {
        if (repository.existsById(id)) {
            rendimiento.setId(id);
            return repository.save(rendimiento);
        }
        return null;
    }

    public boolean eliminar(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
