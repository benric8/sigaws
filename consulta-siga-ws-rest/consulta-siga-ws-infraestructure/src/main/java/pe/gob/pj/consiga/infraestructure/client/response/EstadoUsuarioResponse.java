package pe.gob.pj.consiga.infraestructure.client.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;

@AllArgsConstructor
@Data
public class EstadoUsuarioResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EstadoUsuarioSiga estadoUsuario;

	
}
