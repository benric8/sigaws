package pe.gob.pj.consiga.domain.port.usecase;

import java.util.List;

import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;

public interface SigaUseCasePort {
	public List<EstadoUsuarioSiga> recuperarEstados(String cuo, String dni) throws Exception;
}
