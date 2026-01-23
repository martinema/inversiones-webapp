package com.inversiones.service;

import com.inversiones.entity.TipoRendimiento;
import com.inversiones.repository.TipoRendimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para las operaciones CRUD de TipoRendimiento
 */
@Service
public class TipoRendimientoService {

    @Autowired
    private TipoRendimientoRepository repository;

    public List<TipoRendimiento> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<TipoRendimiento> obtenerPorId(Integer id) {
        return repository.findById(id);
    }

    public TipoRendimiento guardar(TipoRendimiento tipoRendimiento) {
        return repository.save(tipoRendimiento);
    }

    public TipoRendimiento actualizar(Integer id, TipoRendimiento tipoRendimiento) {
        if (repository.existsById(id)) {
            tipoRendimiento.setId(id);
            return repository.save(tipoRendimiento);
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
