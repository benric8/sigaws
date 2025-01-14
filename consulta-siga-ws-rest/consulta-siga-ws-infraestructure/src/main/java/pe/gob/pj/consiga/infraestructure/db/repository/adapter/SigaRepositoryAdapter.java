package pe.gob.pj.consiga.infraestructure.db.repository.adapter;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.procedure.ProcedureCall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import pe.gob.pj.consiga.domain.exceptions.ErrorDaoException;
import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;
import pe.gob.pj.consiga.domain.port.repository.SigaRepositoryPort;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.infraestructure.db.procedures.ProcedureSiga;

@Slf4j
@Component("sigaRepositoryPort")
public class SigaRepositoryAdapter implements SigaRepositoryPort, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("sessionSiga")
	private SessionFactory sf;

	@Override
	public List<EstadoUsuarioSiga> recuperarEstados(String cuo, String dni) throws Exception {
		List<EstadoUsuarioSiga> lista = new ArrayList<EstadoUsuarioSiga>();
		log.info("{} INICIO_DAO CONSULTA ESTADOS PERSONAL", cuo);
		try {
			ProcedureCall call = this.sf.getCurrentSession().createStoredProcedureCall(ProcedureSiga.QUERY_CONSULTAR_ACCESO_DNI);
			call.registerParameter(1, String.class, ParameterMode.IN).bindValue(dni);
			call.registerParameter(2,OracleTypes.class , ParameterMode.REF_CURSOR);
					 
			@SuppressWarnings("unchecked")
			List<Object[]> estados = call.getResultList();
			
		    if (estados != null && !estados.isEmpty()) {
		        for (Object[] estadoRecuperado : estados) {
		            EstadoUsuarioSiga estado = new EstadoUsuarioSiga(
		            		(String) estadoRecuperado[0],
		            		(String) estadoRecuperado[1],
		            		(String) estadoRecuperado[2],
		            		(String) estadoRecuperado[3],
		            		(String) estadoRecuperado[4]);
		            lista.add(estado);
		        }
		    }
					
		} catch (SQLGrammarException | IllegalArgumentException | ConstraintViolationException | DataIntegrityViolationException e) {
			log.error("Error al ejecutar la consulta de estados de personal: {}", e.getMessage());
			throw new ErrorDaoException(ProjectConstants.Error.CE004, 
					ProjectConstants.Error.CE004+ProjectConstants.Error.XE004+ProjectConstants.Proceso.SIGA_CONSULTAR_ESTADO, 
					getJNDI(), 
					e.getMessage(), 
					e.getCause());
		}
		return lista;
	}
	
	public String getJNDI() throws Exception{
		return (SessionFactoryUtils.getDataSource(sf)).getConnection().getMetaData().getURL();
	}
	
	
	

}
