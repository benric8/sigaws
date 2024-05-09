package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpInmuebleDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String registro;
	private String libro;
	private String apPaterno;
	private String apMaterno;
	private String nombre;
	private String tipoDocumento;
	private String numeroDocumento;
	private String numeroPartida;
	private String numeroPlaca;
	private String estado;
	private String zona;
	private String oficina;
	private String direccion;
	private String razonSocial;

}
