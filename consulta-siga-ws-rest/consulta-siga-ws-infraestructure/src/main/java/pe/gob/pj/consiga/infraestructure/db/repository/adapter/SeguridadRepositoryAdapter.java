package pe.gob.pj.consiga.infraestructure.db.repository.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.model.seguridad.Rol;
import pe.gob.pj.consiga.domain.model.seguridad.Usuario;
import pe.gob.pj.consiga.domain.port.repository.SeguridadRepositoryPort;
import pe.gob.pj.consiga.domain.utils.EncryptUtils;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.infraestructure.db.entity.security.MaeRol;
import pe.gob.pj.consiga.infraestructure.db.entity.security.MaeRolUsuario;
import pe.gob.pj.consiga.infraestructure.db.entity.security.MaeUsuario;

@Slf4j
@Component("seguridadRepositoryPort")
public class SeguridadRepositoryAdapter implements SeguridadRepositoryPort{

	@Autowired
	@Qualifier("sessionSeguridad")
	private SessionFactory sf;

	@Override
	public String autenticarUsuario(String cuo, String codigoCliente, String codigoRol, String usuario, String clave) throws Exception {
		Usuario user = new Usuario();
		int nAplicacion = ProjectProperties.getSeguridadIdAplicativo();
		Object[] params = { usuario, codigoRol, nAplicacion, codigoCliente };
		try {			
			TypedQuery<MaeUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeRolUsuario.AUTENTICAR_USUARIO, MaeUsuario.class);
			query.setParameter(MaeRolUsuario.P_COD_USUARIO, usuario);
			query.setParameter(MaeRolUsuario.P_COD_ROL, codigoRol);
			query.setParameter(MaeRolUsuario.P_COD_CLIENTE, codigoCliente);
			query.setParameter(MaeRolUsuario.P_N_APLICATIVO, nAplicacion);
			MaeUsuario usr =  query.getSingleResult();
			String claveFinal = EncryptUtils.encrypt(usuario, clave);
			if(ProjectUtils.isNull(usr.getCClave()).trim().equals(claveFinal)) {
				user.setId(usr.getNUsuario());
				user.setCClave(ProjectUtils.isNull(usr.getCClave()));
			}
		} catch (NoResultException not) {
			log.info(cuo.concat("No se encontro usuario registrado en BD").concat(params.toString()));
		} catch (Exception e) {
			log.error(cuo.concat(e.getMessage()));
		}
		return user.getId() == null? null: user.getId().toString();
	}
	
	@Override
	public Usuario recuperaInfoUsuario(String cuo, String id) throws Exception {
		Usuario user = new Usuario();
		Object[] params = { Integer.parseInt(id) };
		try {
			TypedQuery<MaeUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeUsuario.FIND_BY_ID, MaeUsuario.class);
			query.setParameter(MaeUsuario.P_N_USUARIO, Integer.parseInt(id));
			MaeUsuario u = query.getSingleResult();
			user.setCClave(u.getCClave());
			user.setCUsuario(u.getCUsuario());
			user.setId(u.getNUsuario());
			user.setLActivo(u.getActivo());
		} catch (NoResultException not) {
			log.info(cuo.concat("No se encontro usuario registrado en BD").concat(params.toString()));
			user = null;
		} catch (Exception e) {
			log.error(cuo.concat(e.getMessage()));
			user = null;
		}
		return user;
	}
	
	@Override
	public List<Rol> recuperarRoles(String cuo, String id) throws Exception {
		List<Rol> lista = new ArrayList<Rol>();
		Object[] params = { Integer.parseInt(id) };
		try {
			TypedQuery<MaeRol> query = this.sf.getCurrentSession().createNamedQuery(MaeRol.FIND_ROLES_BY_ID_USUARIO, MaeRol.class);
			query.setParameter(MaeUsuario.P_N_USUARIO, Integer.parseInt(id));
			query.getResultStream().forEach(maeRol -> {
				lista.add(new Rol(maeRol.getNRol(), maeRol.getCRol(), maeRol.getXRol(), maeRol.getActivo()));
			});
		} catch (NoResultException not) {
			log.info(cuo.concat("No se encontro roles registrado en BD").concat(params.toString()));
		} catch (Exception e) {
			log.error(cuo.concat(e.getMessage()));
		}
		return lista;
	}

	@Override
	public String validarAccesoMetodo(String cuo, String usuario, String rol, String operacion) throws Exception {
		StringBuilder rpta = new StringBuilder("");
		Object[] params = {usuario,rol,operacion};
		try {
			TypedQuery<MaeRolUsuario> query = this.sf.getCurrentSession().createNamedQuery(MaeRolUsuario.VALIDAR_ACCESO_METODO , MaeRolUsuario.class);
			query.setParameter(MaeRolUsuario.P_COD_USUARIO, usuario);
			query.setParameter(MaeRolUsuario.P_COD_ROL, rol);
			query.setParameter(MaeRolUsuario.P_OPERACION, operacion);
			MaeRolUsuario rolusuario = query.getResultStream().findFirst().orElse(null);
			if(rolusuario!=null) {
				rolusuario.getMaeRol().getMaeOperacions().forEach(x->{
					if(x.getXEndpoint().equalsIgnoreCase(operacion))
						rpta.append(x.getXOperacion());
				});
			}
		} catch (NoResultException not) {
			log.info(cuo.concat("No se encontro permiso a la operacion con el rol del usuario").concat(params.toString()));
		} catch (Exception e) {
			log.error(cuo.concat(e.getMessage()));
		}		
		return rpta.toString();
	}
}
