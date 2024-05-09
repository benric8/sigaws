package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ConadisBuscarPesonaBodyResponse implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("Nombre")
	private String nombre;
	
	@JsonProperty("ApellidoPaterno")
	private String apellidoPaterno;
	
	@JsonProperty("ApellidoMaterno")
	private String apellidoMaterno;
	
	@JsonProperty("Fallecido")
	private boolean fallecido;
	
	@JsonProperty("Gravedad")
	private Integer gravedad;
	
	@JsonProperty("Estado")
	private Integer estado;

}
