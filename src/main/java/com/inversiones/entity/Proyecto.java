package com.inversiones.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Proyectos Almacena información de los proyectos de inversión
 */
@Entity
@Table(name = "Proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

	@Column(name = "Inmueble", nullable = false, length = 255)
	private String inmueble;

	@Column(name = "Moneda", nullable = false, length = 10)
	private String moneda;

	@Column(name = "ID_Tipo_rendimiento", nullable = false)
	private Integer idTipoRendimiento;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_Tipo_rendimiento", referencedColumnName = "ID", insertable = false, updatable = false)
	private TipoRendimiento tipoRendimiento;

	@Column(name = "Estado", length = 50)
	private String estado;

	@Column(name = "Duracion")
	private Double duracion;

	@Column(name = "Estatus", length = 50)
	private String estatus;

	@Column(name = "rentabilidad_mensual_reentel")
	private Double rentabilidadMensualReentel;

	@Column(name = "rentabilidad_mensual_reentel_pro")
	private Double rentabilidadMensualReentelPro;

	@Column(name = "rentabilidad_mensual_reentel_super")
	private Double rentabilidadMensualReentelSuper;

	@Column(name = "rentabilidad_anual_reentel")
	private Double rentabilidadAnualReentel;

	@Column(name = "rentabilidad_anual_reentel_pro")
	private Double rentabilidadAnualReentelPro;

	@Column(name = "rentabilidad_anual_reentel_super")
	private Double rentabilidadAnualReentelSuper;

	@Column(name = "rentabilidad_total_reentel")
	private Double rentabilidadTotalReentel;

	@Column(name = "rentabilidad_total_reentel_pro")
	private Double rentabilidadTotalReentelPro;

	@Column(name = "rentabilidad_total_reentel_super")
	private Double rentabilidadTotalReentelSuper;

	@Column(name = "fecha_entrada")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaEntrada;

	@Column(name = "fecha_cierre")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaCierre;

	@Column(name = "fecha_fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaFin;

	@Column(name = "fecha_inicio_renta")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaInicioRenta;

	@Column(name = "importe_inversion_eur")
	private Double importeInversionEur;

	@Column(name = "importe_inversion_dol")
	private Double importeInversionDol;

	@Column(name = "numero_tokens")
	private Double numeroTokens;

	@Column(name = "colateralizable")
	private Boolean colateralizable;
}
