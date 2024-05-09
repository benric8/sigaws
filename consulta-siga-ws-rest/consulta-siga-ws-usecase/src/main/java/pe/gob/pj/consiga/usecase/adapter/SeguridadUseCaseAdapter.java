package pe.gob.pj.consiga.usecase.adapter;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import pe.gob.pj.consiga.domain.model.seguridad.Rol;
import pe.gob.pj.consiga.domain.model.seguridad.Usuario;
import pe.gob.pj.consiga.domain.port.repository.SeguridadRepositoryPort;
import pe.gob.pj.consiga.domain.port.usecase.SeguridadUseCasePort;

@Service("seguridadUseCasePort")
public class SeguridadUseCaseAdapter implements SeguridadUseCasePort{

	@Autowired
	private SeguridadRepositoryPort persistence;	
	
	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class})
	public String autenticarUsuario(String cuo, String codigoCliente, String codigoRol, String usuario, String clave)
			throws Exception {
		return persistence.autenticarUsuario(cuo, codigoCliente, codigoRol, usuario, clave);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class})
	public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception {
		return persistence.recuperaInfoUsuario(cuo, id);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class})
	public List<Rol> recuperarRoles(String cuo, String id) throws Exception {
		return persistence.recuperarRoles(cuo, id);
	}

	@Override
	@Transactional(transactionManager = "txManagerSeguridad", propagation = Propagation.REQUIRES_NEW, readOnly = true, rollbackFor = { Exception.class, SQLException.class})
	public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion) throws Exception {
		return persistence.validarAccesoMetodo(cuo, usuario, rol, operacion);
	}

}
