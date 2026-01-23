package com.inversiones.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Tipo_rendimiento
 * Almacena los tipos de rendimiento disponibles
 */
@Entity
@Table(name = "Tipo_rendimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoRendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "Tipo", nullable = false, length = 100)
    private String tipo;
}
