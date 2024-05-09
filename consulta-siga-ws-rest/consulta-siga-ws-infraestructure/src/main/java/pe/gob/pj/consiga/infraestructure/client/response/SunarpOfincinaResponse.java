package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpOficinaDto;

@Data
public class SunarpOfincinaResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<SunarpOficinaDto> oficina;

}
