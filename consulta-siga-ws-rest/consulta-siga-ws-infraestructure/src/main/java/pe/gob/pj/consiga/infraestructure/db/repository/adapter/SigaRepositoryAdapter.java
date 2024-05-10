package pe.gob.pj.consiga.infraestructure.db.repository.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;
import pe.gob.pj.consiga.domain.port.repository.SigaRepositoryPort;

@Slf4j
@Component("sigaRepositoryPort")
public class SigaRepositoryAdapter implements SigaRepositoryPort{@Override
	public List<EstadoUsuarioSiga> recuperarRoles(String cuo, String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
