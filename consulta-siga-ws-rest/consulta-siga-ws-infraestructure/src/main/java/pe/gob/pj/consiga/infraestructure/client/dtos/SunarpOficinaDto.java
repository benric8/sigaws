package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpOficinaDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codZona;
	private String codOficina;
	private String descripcion;

}
