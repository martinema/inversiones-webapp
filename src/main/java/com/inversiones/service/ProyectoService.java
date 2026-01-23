package com.inversiones.service;

import com.inversiones.entity.Proyecto;
import com.inversiones.repository.ProyectoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para las operaciones CRUD de Proyecto
 */
@Service
public class ProyectoService {

    @Autowired
    private ProyectoRepository repository;

    public List<Proyecto> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<Proyecto> obtenerPorId(Integer id) {
        return repository.findById(id);
    }

    public Proyecto guardar(Proyecto proyecto) {
        return repository.save(proyecto);
    }

    public Proyecto actualizar(Integer id, Proyecto proyecto) {
        if (repository.existsById(id)) {
            proyecto.setId(id);
            return repository.save(proyecto);
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
