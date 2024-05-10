package pe.gob.pj.consiga.domain.port.repository;

import java.util.List;


import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;

public interface SigaRepositoryPort {
	public List<EstadoUsuarioSiga> recuperarRoles(String cuo, String id) throws Exception;
}
