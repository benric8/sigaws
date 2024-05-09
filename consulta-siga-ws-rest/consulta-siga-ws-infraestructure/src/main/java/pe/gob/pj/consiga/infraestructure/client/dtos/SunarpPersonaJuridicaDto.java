package pe.gob.pj.consiga.infraestructure.client.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class SunarpPersonaJuridicaDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String zona;
	private String oficina;
	private BigDecimal partida;
	private String tipo;
	private String denominacion;

}
