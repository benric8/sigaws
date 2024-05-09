package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpAsientoDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idImgAsiento;
	private Integer numPag;
	private String tipo;
	private Object listPag;

}
