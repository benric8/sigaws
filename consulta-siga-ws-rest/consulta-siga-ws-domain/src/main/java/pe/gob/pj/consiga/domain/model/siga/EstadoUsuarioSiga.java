package pe.gob.pj.consiga.domain.model.siga;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstadoUsuarioSiga implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoValidacion;
	private String descripcionValidacion;
	private String nombres;
	private String apellidoPaterno;
	private String apellidoMaterno;

}
