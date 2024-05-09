package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class SunarpBuscarPersonaJuridicaResponse implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SunarpPersonaJuridicaResponse personaJuridica;

}
