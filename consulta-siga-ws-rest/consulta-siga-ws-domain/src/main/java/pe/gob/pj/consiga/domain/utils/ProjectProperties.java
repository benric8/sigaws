package pe.gob.pj.consiga.domain.utils;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(value= {
		@PropertySource("classpath:consulta-siga-ws.properties")
})
public class ProjectProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String seguridadSecretToken;
    private static Integer seguridadIdAplicativo;
    private static Integer seguridadTiempoExpiraSegundos;
    private static Integer seguridadTiempoRefreshSegundos;
    private static int timeoutBdTransactionSegundos;
   
    @Autowired
    public ProjectProperties(
            @Value("${configuracion.seguridad.secretToken:null}") String seguridadSecretToken,
            @Value("${configuracion.seguridad.idaplicativo:0}") Integer seguridadIdAplicativo,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.expira.segundos:300}") Integer seguridadTiempoExpiraSegundos,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.refresh.segundos:180}") Integer seguridadTiempoRefreshSegundos,
            @Value("${timeout.database.transaction.segundos:120}") int timeoutBdTransactionSegundos
    		) {

        ProjectProperties.seguridadSecretToken = seguridadSecretToken;
        ProjectProperties.seguridadIdAplicativo = seguridadIdAplicativo;
        ProjectProperties.seguridadTiempoExpiraSegundos = seguridadTiempoExpiraSegundos;
        ProjectProperties.seguridadTiempoRefreshSegundos = seguridadTiempoRefreshSegundos;
        ProjectProperties.timeoutBdTransactionSegundos = timeoutBdTransactionSegundos;
       
    }

	public static String getSeguridadSecretToken() {
		return seguridadSecretToken;
	}

	public static Integer getSeguridadIdAplicativo() {
		return seguridadIdAplicativo;
	}

	public static Integer getSeguridadTiempoExpiraSegundos() {
		return seguridadTiempoExpiraSegundos;
	}

	public static Integer getSeguridadTiempoRefreshSegundos() {
		return seguridadTiempoRefreshSegundos;
	}

	public static int getTimeoutBdTransactionSegundos() {
		return timeoutBdTransactionSegundos;
	}

}
