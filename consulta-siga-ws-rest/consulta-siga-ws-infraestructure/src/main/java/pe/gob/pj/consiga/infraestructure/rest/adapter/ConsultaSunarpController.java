package pe.gob.pj.consiga.infraestructure.rest.adapter;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.exceptions.ErrorException;
import pe.gob.pj.consiga.domain.port.usecase.ConsultaUseCasePort;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientoImagenResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientosResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarOficinasResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarPersonaJuridicaResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarTitularidadBodyResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarVehiculoDetalleResponse;
import pe.gob.pj.consiga.infraestructure.client.services.SunarpClient;
import pe.gob.pj.consiga.infraestructure.rest.response.GlobalResponse;

@Slf4j
@RestController
@RequestMapping(value="sunarp", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class ConsultaSunarpController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("consultaUseCasePort")
	private ConsultaUseCasePort consultaApp;
	
	@Autowired
	@Qualifier("sunarpClient")
	private SunarpClient clientSunarp;
	
	@GetMapping(value="buscar/oficinas")
	public ResponseEntity<GlobalResponse> buscarOficinas(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			SunarpBuscarOficinasResponse data = clientSunarp.buscarOficinas(cuo);
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion(ProjectConstants.Error.XEXITO);
			res.setData(data);
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_OFICINAS + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping(value="buscar/titularidad")
	public ResponseEntity<GlobalResponse> buscarTitularidad(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta,
			@Pattern(regexp = ProjectConstants.PatternServicios.SUNARP_TIPO_PARTICIPANTE, message = "El parámetro tipoParticipante no tiene formato valido.")
			@NotBlank(message = "El parámetro tipoParticipante no puede tener un valor vacio.")
			@Length(min = 1, max = 1, message = "El parámetro tipoParticipante tiene un tamaño no valido [min=1,max=1].") 
			@RequestParam(name="tipoParticipante", required = true)
			String tipoParticipante,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHA, message = "El parámetro apellidoPaterno solo permite letras.")
			@NotBlank(message = "El parámetro apellidoPaterno no puede tener un valor vacio.")
			@Length(min = 1, max = 1, message = "El parámetro apellidoPaterno tiene un tamaño no valido [min=1,max=1].") 
			@RequestParam(name="apellidoPaterno", required = false)
			String apellidoPaterno,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHA, message = "El parámetro apellidoMaterno solo permite letras.")
			@NotBlank(message = "El parámetro apellidoMaterno no puede tener un valor vacio.")
			@Length(min = 1, max = 1, message = "El parámetro apellidoMaterno tiene un tamaño no valido [min=1,max=1].") 
			@RequestParam(name="apellidoMaterno", required = false)
			String apellidoMaterno,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHA, message = "El parámetro nombres solo permite letras.")
			@NotBlank(message = "El parámetro nombres no puede tener un valor vacio.")
			@Length(min = 1, max = 1, message = "El parámetro nombres tiene un tamaño no valido [min=1,max=1].") 
			@RequestParam(name="nombres", required = false)
			String nombres,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro razonSocial solo permite letras.")
			@NotBlank(message = "El parámetro razonSocial no puede tener un valor vacio.")
			@RequestParam(name="razonSocial", required = false)
			String razonSocial){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			SunarpBuscarTitularidadBodyResponse data = clientSunarp.buscarTituaridad(cuo, tipoParticipante, 
					ProjectUtils.isNull(apellidoPaterno), ProjectUtils.isNull(apellidoMaterno), ProjectUtils.isNull(nombres), 
					ProjectUtils.isNull(razonSocial));
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion(ProjectConstants.Error.XEXITO);
			res.setData(data);
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_TITULARIDAD + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping(value="buscar/asientos")
	public ResponseEntity<GlobalResponse> buscarAsientos(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro registro no tiene formato valido.")
			@NotBlank(message = "El parámetro registro no puede tener un valor vacio.")
			@Length(min = 1, max = 5, message = "El parámetro zona tiene un tamaño no valido [min=1,max=5].") 
			@RequestParam(name="registro", required = true)
			String registro,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El zona apellidoPaterno solo permite letras.")
			@NotBlank(message = "El parámetro zona no puede tener un valor vacio.")
			@Length(min = 1, max = 2, message = "El parámetro zona tiene un tamaño no valido [min=1,max=2].") 
			@RequestParam(name="zona", required = true)
			String zona,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro oficina solo permite letras.")
			@NotBlank(message = "El parámetro oficina no puede tener un valor vacio.")
			@Length(min = 1, max = 2, message = "El parámetro oficina tiene un tamaño no valido [min=1,max=2].") 
			@RequestParam(name="oficina", required = false)
			String oficina,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro partida solo permite letras.")
			@NotBlank(message = "El parámetro partida no puede tener un valor vacio.")
			@RequestParam(name="partida", required = true)
			String partida){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			try {
				SunarpBuscarAsientosResponse data = clientSunarp.buscarAsientos(cuo, registro, zona, oficina, partida);
				res.setCodigo(ProjectConstants.Error.CEXITO);
				res.setDescripcion(ProjectConstants.Error.XEXITO);
				res.setData(data);
			} catch (ErrorException e){
				throw e;
			} catch (Exception e) {
				throw new ErrorException(ProjectConstants.Error.CE000, 
						ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTOS+ProjectConstants.Error.XE000,
						e.getMessage(),
						e.getCause());
			}	
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTOS + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping(value="buscar/asiento/img")
	public ResponseEntity<GlobalResponse> buscarImagenAsiento(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta,
			@Pattern(regexp = ProjectConstants.Pattern.NUMBER, message = "El parámetro transaccion no tiene formato valido.")
			@RequestParam(name="transaccion", required = true)
			String transaccion,
			@Pattern(regexp = ProjectConstants.Pattern.NUMBER, message = "El parámetro idImg no tiene formato valido.")
			@RequestParam(name="idImg", required = true)
			String idImg,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHA, message = "El parámetro tipo solo permite letras.")
			@RequestParam(name="tipo", required = true)
			String tipo,
			@Pattern(regexp = ProjectConstants.Pattern.NUMBER, message = "El parámetro nroTotalPag no tiene formato valido.")
			@RequestParam(name="nroTotalPag", required = true)
			String nroTotalPag,
			@Pattern(regexp = ProjectConstants.Pattern.NUMBER, message = "El parámetro nroPagRef no tiene formato valido.")
			@RequestParam(name="nroPagRef", required = true)
			String nroPagRef,
			@Pattern(regexp = ProjectConstants.Pattern.FLAG_10, message = "El parámetro pagina solo permite letras.")
			@RequestParam(name="pagina", required = true)
			String pagina){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			SunarpBuscarAsientoImagenResponse data = clientSunarp.buscarAsientoImagen(cuo, transaccion, idImg, tipo, nroTotalPag, nroPagRef, pagina);
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion(ProjectConstants.Error.XEXITO);
			res.setData(data);	
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTO_IMG + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping(value="buscar/vehiculo/detalle")
	public ResponseEntity<GlobalResponse> buscarVehiculoDetalle(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro zona no tiene formato valido.")
			@NotBlank(message = "El parámetro zona no puede tener un valor vacio.")
			@Length(min = 1, max = 2, message = "El parámetro zona tiene un tamaño no valido [min=1,max=2].") 
			@RequestParam(name="zona", required = true)
			String zona,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro oficina solo permite letras.")
			@NotBlank(message = "El parámetro oficina no puede tener un valor vacio.")
			@Length(min = 1, max = 2, message = "El parámetro oficina tiene un tamaño no valido [min=1,max=2].") 
			@RequestParam(name="oficina", required = true)
			String oficina,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro placa solo permite letras.")
			@NotBlank(message = "El parámetro placa no puede tener un valor vacio.")
			@RequestParam(name="placa", required = false)
			String placa){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			SunarpBuscarVehiculoDetalleResponse data = clientSunarp.buscarVehiculoDetalle(cuo, zona, oficina, placa);
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion(ProjectConstants.Error.XEXITO);
			res.setData(data);
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_VEHICULO_DETALLE + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	@GetMapping(value="buscar/persona-juridica")
	public ResponseEntity<GlobalResponse> buscarPersonaJuridica(@RequestAttribute(name=ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta,
			@Pattern(regexp = ProjectConstants.Pattern.ALPHANUMBER, message = "El parámetro razonSocial solo permite letras.")
			@NotBlank(message = "El parámetro razonSocial no puede tener un valor vacio.")
			@Length(min = 1, max = 1, message = "El parámetro razonSocial tiene un tamaño no valido [min=1,max=1].") 
			@RequestParam(name="razonSocial", required = true)
			String razonSocial){
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {
			SunarpBuscarPersonaJuridicaResponse data = clientSunarp.buscarPersonaJuridica(cuo, razonSocial);
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion(ProjectConstants.Error.XEXITO);
			res.setData(data);	
		} catch (ErrorException e) {
			handleException(cuo, e, res);
		} catch (Exception e) {
			handleException(cuo, new ErrorException(ProjectConstants.Error.CE000, 
			        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_PERSONA_JURIDICA + ProjectConstants.Error.XE000,
			        e.getMessage(),
			        e.getCause()), res);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(!ProjectConstants.FormatoRespuesta.XML.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
	private void handleException(String cuo, ErrorException e, GlobalResponse res) {
	    res.setCodigo(e.getCodigo());
	    res.setDescripcion(e.getDescripcion());
	    log.error("{} {} | {} | {} | {} | {} | {}", cuo, ProjectConstants.Error.TRAZA_LOG, res.getCodigo(), res.getDescripcion(), ProjectUtils.obtenerClaseMetodoLineaException(e), e.getMessage(), ProjectUtils.obtenerCausaException(e));
	}
	
}
