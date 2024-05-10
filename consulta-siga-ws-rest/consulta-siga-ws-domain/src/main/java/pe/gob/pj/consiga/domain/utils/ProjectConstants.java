package pe.gob.pj.consiga.domain.utils;

public class ProjectConstants {

	public static final String METHOD_CORTA_ULTIMA_BARRA_INVERTIDA = "PUT";
	
	public static final int DEFAULT_PAGINATION_PAGE_SIZE = 10;
	
	public static final int NRO_VECES_REFRESH_CON_TOKEN_EXPIRADO = 100;
	
	public class Aplicativo {
		public static final String NOMBRE = "consulta-siga-ws";
		public static final String VERSION = "1.0.0";
	}
	
	public class Auditoria {
		public static final String B_INSTER = "I";
		public static final String B_UPDATE = "U";
	}
	
	public class Estado {
		public static final String ACTIVO = "1";
		public static final String INACTIVO = "0";
		public static final String ACTIVO_S = "S";
		public static final String INACTIVO_N = "S";
	}

	public class Pattern {
		public static final String NUMBER = "\\d+";
		public static final String ALPHANUMBER = "[a-zA-Z0-9]+";
		public static final String ALPHA = "[a-zA-Z]+";
		public static final String ALPHANUMBER_COMAS = "[a-zA-Z0-9]+(,[a-zA-Z0-9]+)*";
		public static final String S_N = "[SN]";
		public static final String FLAG_10 = "[10]";
		public static final String FORMATO_EXPEDIENTE = "(\\d{5})[-](\\d{4})[-](\\d{1,4})[-](\\d{4})[-]([A-Za-z]{2})[-]([A-Za-z]{2})[-](\\d{2})";
		public static final String FECHA = "([0][1-9]|[1-2][0-9]|[3][0-1])/([0][1-9]|[1][0-2])/(\\d{4})";
		public static final String FECHA_DD_MM_YYYY_HH_MM_SS_SSS = "(\\d{2})/(\\d{2})/(\\d{4}) (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})";
		public static final String FECHA_YYYY_MM_DD_HH_MM_SS_SSS = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})\\.(\\d{3})";
		public static final String EMAIL = "([A-Za-z0-9]+[._-]?[A-Za-z0-9]+)+@([A-Za-z0-9]+[-]?[A-Za-z0-9]+\\.)*[A-Za-z0-9]{2,6}";
		public static final String IP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		public static final String MAC = "([0-9A-F]{2}[:-]){5}([0-9A-F]{2})";
		public static final String FECHA_SBS = "^(0[1-9]|1[0-9]|2[0-9]|3[0-1])(0[1-9]|1[0-2])([0-9]{4})$";
		
	}
	
	public class PatternServicios {
		public static final String SUNARP_TIPO_PARTICIPANTE = "[NJnj]";
	}

	public class Formato {
		public static final String FECHA_YYYYMMDD = "yyyyMMdd";
		public static final String FECHA_YYYY_MM_DD = "yyyy/MM/dd";
		public static final String FECHA_YYYY_MM_DD_ = "yyyy-MM-dd";
		public static final String FECHA_DD_MM_YYYY = "dd/MM/yyyy";
		public static final String FECHA_DD_MM_YYYY_HH_MM = "dd/MM/yyyy hh:mm a";
		public static final String FECHA_DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";
		public static final String FECHA_DD_MM_YYYY_HH_MM_SS_SSS="dd/MM/yyyy HH:mm:ss.SSS";
		public static final String FECHA_YYYY_MM_DD_HH_MM_SS_SSS="yyyy-MM-dd HH:mm:ss.SSS";
	}
	
	public static final String AUD_CUO = "auditoriaCuo";
	public static final String AUD_IP = "auditoriaIp";
	public static final String AUD_JWT = "auditoriaJwt";
	public static final String REMOTE_IP = "ipRemota";
	public static final String TOKEN_ACCESO_EXTERNO = "out";
	public static final String TOKEN_ACCESO_INTERNO = "in";
	public static final String TOKEN_ACCESO_NEUTRO = "io";
	public static final String TOKEN_OPERACION_SPLIT = "@";

	public class Claim {
		public static final String ROL = "rol";
		public static final String USUARIO = "usuario";
		public static final String IP = "remoteIp";
		public static final String NUMERO = "numero";
		public static final String ACCESO = "log_";
		public static final String LIMIT= "limit";
	}

	public class FormatoRespuesta{
		public static final String XML = "xml";
		public static final String JSON = "json";
	}
	
	public class Error {
		public static final String CERROR = "E";
		public static final String CEXITO = "0000";
		public static final String C0001 = "0001";
		public static final String CE000 = "E000";
		public static final String CE001 = "E001";
		public static final String CE002 = "E002";
		public static final String CE003 = "E003";
		public static final String CE004 = "E004";
		public static final String CE005 = "E005";
		public static final String CE006 = "E006";
		public static final String CE007 = "E007";
		public static final String CE008 = "E008";
		public static final String CE009 = "E009";
		public static final String CE010 = "E010";
		public static final String CE011 = "E011";
		public static final String CE012 = "E012";
		public static final String CE013 = "E013";

		public static final String XEXITO = "La operación se realizo de manera exitosa.";
		public static final String X0001 = "Datos no encontrados al ";
		public static final String XE000 = " - Error inesperado.";
		public static final String XE001 = "Lo sentimos, la sesión ha expirado.";
		public static final String XE002 = "Lo sentimos, hubo un problema al validar el token.";
		public static final String XE003 = "Error al generar un nuevo token, el tiempo límite para refrescar el token enviado a expirado.";
		public static final String XERROR = "Error al ";
		public static final String XE004 = " - Problemas en la ejecución del SP: ";
		public static final String XE005 = " - No se estableció conexión con la BD PJ Seguridad.";
		public static final String XE006 = " - No se puede obtener datos de configuración de la BD: ";
		public static final String XE007 = " - Problemas con las credenciales de conexión a la BD: ";
		public static final String XE008 = " - Problemas en la conexión con la BD: ";
		public static final String XE009 = " - No se pudo subir documento al ftp indicado. ";
		public static final String XE010 = " - No se pudo descargar documento del ftp indicado.";
		public static final String XE011 = " - No se pudo subir documento al alfresco indicado.";
		public static final String XE012 = " - No se pudo descargar documento del alfresco indicado.";
		public static final String XE013 = " - Problemas con el consumo del endpoint: ";

		public static final String TRAZA_LOG = "TRAZA_LOG";
		public static final String CAUSA_NO_IDENTIFICADA = "Causa: No se puede identificar.";
		public static final String CML_NO_IDENTIFICADA = "Clase-Metodo-Linea: No se puede identificar.";
		public static final String FORMAT_RES_NO_IDENTIFICADA = "Formato Respuesta: Error al convertir en formato xml.";
	}
	
	public class Proceso {
		public static final String SIGA_CONSULTAR_ESTADO = "Consultar Estado del Personal en el SIGA";
		}
	
	public class Mensajes {
		public static final String MSG_ERROR_GENERICO_CONVERSION = "El tipo de dato de entrada es incorrecto";
	}

}
