package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SunarpBuscarTirularidadResponse implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("respuestaTitularidad")
	private SunarpTitularidadResponse respuestaTitularidad;

}
