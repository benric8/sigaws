package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpFolioDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer idImgFolio;
	private Integer nroPagRef;
	private Integer pagina;
	private String tipo;
	
}
