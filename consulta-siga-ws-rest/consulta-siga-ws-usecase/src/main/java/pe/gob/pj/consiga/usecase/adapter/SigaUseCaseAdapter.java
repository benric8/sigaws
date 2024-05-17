package pe.gob.pj.consiga.usecase.adapter;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;
import pe.gob.pj.consiga.domain.port.repository.SigaRepositoryPort;
import pe.gob.pj.consiga.domain.port.usecase.SigaUseCasePort;


@Slf4j
@Service("sigaUseCasePort")
public class SigaUseCaseAdapter implements SigaUseCasePort, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("sigaRepositoryPort")
	private SigaRepositoryPort repo;

	@Override
	@Transactional(transactionManager = "txManagerSiga", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class})
	public List<EstadoUsuarioSiga> recuperarEstados(String cuo, String dni) throws Exception {
		log.info("{} INICIO_SERVICE CONSULTA SIGA", cuo);
		List<EstadoUsuarioSiga> lista = repo.recuperarEstados(cuo, dni);
		log.info("{} FIN_SERVICE CONSULTA SIGA", cuo);
		return lista;
			
	}

}
