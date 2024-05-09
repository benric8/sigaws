package pe.gob.pj.consiga.infraestructure.client.services;

import pe.gob.pj.consiga.infraestructure.client.response.ConadisBuscarPesonaBodyResponse;

public interface ConadisClient {
	public ConadisBuscarPesonaBodyResponse buscarPersonaDiscapacidad(String cuo, String numeroDocumentoIdentidad) throws Exception;
}
