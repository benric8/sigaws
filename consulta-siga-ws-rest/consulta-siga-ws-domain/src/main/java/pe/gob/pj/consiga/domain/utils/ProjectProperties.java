package pe.gob.pj.consiga.domain.utils;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(value= {
		@PropertySource("classpath:consultaServiciosPide-ws.properties")
})
public class ProjectProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String seguridadSecretToken;
    private static Integer seguridadIdAplicativo;
    private static Integer seguridadTiempoExpiraSegundos;
    private static Integer seguridadTiempoRefreshSegundos;
    private static int timeoutBdTransactionSegundos;
    private static int timeoutClientApiConectionSegundos;
    private static int timeoutClientApiReadSegundos;
    private static String servicioSunarpUrl;
    private static String servicioSunarpUsuario;
    private static String servicioSunarpClave;
    private static String servicioConadisUrl;
    private static String servicioConadisUsuario;
    private static String servicioConadisClave;
	private static String servicioSbsUrl;
	private static String servicioSbsUsuario;
	private static String servicioSbsClave;

    @Autowired
    public ProjectProperties(
            @Value("${configuracion.seguridad.secretToken:null}") String seguridadSecretToken,
            @Value("${configuracion.seguridad.idaplicativo:0}") Integer seguridadIdAplicativo,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.expira.segundos:300}") Integer seguridadTiempoExpiraSegundos,
            @Value("${configuracion.seguridad.authenticate.token.tiempo.refresh.segundos:180}") Integer seguridadTiempoRefreshSegundos,
            @Value("${timeout.database.transaction.segundos:120}") int timeoutBdTransactionSegundos,
            @Value("${timeout.client.api.conection.segundos:60}") int timeoutClientApiConectionSegundos,
            @Value("${timeout.client.api.read.segundos:60}") int timeoutClientApiReadSegundos,
            @Value("${servicio.sunarp.url:null}") String servicioSunarpUrl,
            @Value("${servicio.sunarp.seguridad.usuario:null}") String servicioSunarpUsuario,
            @Value("${servicio.sunarp.seguridad.clave:null}") String servicioSunarpClave,
            @Value("${servicio.conadis.url:null}") String servicioConadisUrl,
            @Value("${servicio.conadis.seguridad.usuario:null}") String servicioConadisUsuario,
            @Value("${servicio.conadis.seguridad.clave:null}") String servicioConadisClave,
			@Value("${servicio.sbs.url:null}") String servicioSbsUrl) {

        ProjectProperties.seguridadSecretToken = seguridadSecretToken;
        ProjectProperties.seguridadIdAplicativo = seguridadIdAplicativo;
        ProjectProperties.seguridadTiempoExpiraSegundos = seguridadTiempoExpiraSegundos;
        ProjectProperties.seguridadTiempoRefreshSegundos = seguridadTiempoRefreshSegundos;
        ProjectProperties.timeoutBdTransactionSegundos = timeoutBdTransactionSegundos;
        ProjectProperties.timeoutClientApiConectionSegundos = timeoutClientApiConectionSegundos;
        ProjectProperties.timeoutClientApiReadSegundos = timeoutClientApiReadSegundos;
        ProjectProperties.servicioSunarpUrl = servicioSunarpUrl;
        ProjectProperties.servicioSunarpUsuario = servicioSunarpUsuario;
        ProjectProperties.servicioSunarpClave = servicioSunarpClave;
        ProjectProperties.servicioConadisUrl = servicioConadisUrl;
        ProjectProperties.servicioConadisUsuario = servicioConadisUsuario;
        ProjectProperties.servicioConadisClave = servicioConadisClave;
		ProjectProperties.servicioSbsUrl = servicioSbsUrl;
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

	public static int getTimeoutClientApiConectionSegundos() {
		return timeoutClientApiConectionSegundos;
	}

	public static int getTimeoutClientApiReadSegundos() {
		return timeoutClientApiReadSegundos;
	}

	public static String getServicioSunarpUrl() {
		return servicioSunarpUrl;
	}

	public static String getServicioSunarpUsuario() {
		return servicioSunarpUsuario;
	}

	public static String getServicioSunarpClave() {
		return servicioSunarpClave;
	}

	public static String getServicioConadisUrl() { return servicioConadisUrl; }

	public static String getServicioConadisUsuario() {
		return servicioConadisUsuario;
	}

	public static String getServicioConadisClave() {
		return servicioConadisClave;
	}

	public static String getServicioSbsUrl() { return servicioSbsUrl; }


}
