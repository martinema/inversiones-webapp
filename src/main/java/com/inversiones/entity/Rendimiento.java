package com.inversiones.entity;

import java.time.LocalDate;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Rendimientos
 * Almacena información de los rendimientos obtenidos
 */
@Entity
@Table(name = "Rendimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rendimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "dividend", length = 255)
    private String dividend;

    @Column(name = "hashid", length = 255)
    private String hashid;

    @Column(name = "wallet", length = 255)
    private String wallet;

    @Column(name = "distributed")
    private Double distributed;

    @Column(name = "retained")
    private Double retained;

    @Column(name = "reinvested")
    private Double reinvested;

    @Column(name = "claimed")
    private Double claimed;

    @Column(name = "claims", columnDefinition = "LONGTEXT")
    private String claims;

    @Column(name = "reinvests", columnDefinition = "LONGTEXT")
    private String reinvests;

    @Column(name = "date")
    private LocalDate date;
}
