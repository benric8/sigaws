package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.Data;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpAsientoImagenDto;

@Data
public class SunarpBuscarAsientoImagenResponse implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SunarpAsientoImagenDto verAsientoSIRSARPResponse;

}
