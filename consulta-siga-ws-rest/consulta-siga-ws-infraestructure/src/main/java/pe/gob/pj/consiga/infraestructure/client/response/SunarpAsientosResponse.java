package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpAsientosResponse implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String transaccion;
	private Integer nroTotalPag;
	private Object listAsientos;
	private Object listFichas;
	private Object listFolios;

}
