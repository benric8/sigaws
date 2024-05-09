package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.Data;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpVehiculoDto;

@Data
public class SunarpVehiculoDetalleResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SunarpVehiculoDto vehiculo;

}
