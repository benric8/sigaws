package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpListarAsientosResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SunarpAsientosResponse asientos;
}
