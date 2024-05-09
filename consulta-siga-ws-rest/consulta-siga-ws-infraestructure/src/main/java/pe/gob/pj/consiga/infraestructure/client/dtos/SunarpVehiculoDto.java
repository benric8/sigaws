package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpVehiculoDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String placa;
	private String serie;
	private String vin;
	private String nro_motor;
	private String color;
	private String marca;
	private String modelo;
	private String estado;
	private String sede;
	private String anoFabricacion;
	private String codCategoria;
	private String codTipoCarr;
	private String carroceria;
	private SunarpPropietarioDto propietarios;

}
