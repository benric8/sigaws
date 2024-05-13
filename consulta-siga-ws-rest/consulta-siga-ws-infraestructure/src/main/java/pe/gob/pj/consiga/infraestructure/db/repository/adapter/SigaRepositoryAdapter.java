package pe.gob.pj.consiga.infraestructure.db.repository.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ParameterMode;

import org.hibernate.SessionFactory;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.model.siga.EstadoUsuarioSiga;
import pe.gob.pj.consiga.domain.port.repository.SigaRepositoryPort;
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

			call.registerParameter(2, Class.class, ParameterMode.REF_CURSOR);
					 
					/*
					 * Output output = call.getOutputs().getCurrent(); if (output.isResultSet()) {
					 * List<Object[]> postComments = ((ResultSetOutput) output).getResultList(); }
					 */
			List<Object[]> result = call.getResultList();

		    // Procesar los resultados
		    if (result != null && !result.isEmpty()) {
		        for (Object[] row : result) {
		            // Suponiendo que cada fila contiene los datos de un EstadoUsuarioSiga
		            EstadoUsuarioSiga estado = new EstadoUsuarioSiga((String) row[0],(String) row[1]);
		            
		            //estado.setCodigoValida((String) row[0]); // Por ejemplo, si row[0] contiene algo relevante
		            lista.add(estado);
		        }
		    }
					
		} catch (Exception e) {
			log.error("Error al ejecutar la consulta de estados de personal: {}", e.getMessage());
		}
		return lista;
	}
	
	
	
	

}
