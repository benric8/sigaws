package pe.gob.pj.consiga.infraestructure.client.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.exceptions.ErrorException;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpAsientoDto;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpFichaDto;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpFolioDto;
import pe.gob.pj.consiga.infraestructure.client.dtos.SunarpInmuebleDto;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientoImagenResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientosResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarOficinasResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarPersonaJuridicaResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarTitularidadBodyResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarVehiculoDetalleResponse;

@Slf4j
@Service("sunarpClient")
public class SunarpClientImpl implements SunarpClient{
	
	@Autowired
    private RestTemplate restTemplate;

	@Override
	public SunarpBuscarOficinasResponse buscarOficinas(String cuo)  throws Exception{
		
		SunarpBuscarOficinasResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServicegetOficinas";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
                .queryParam("clave", ProjectProperties.getServicioSunarpClave());
		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		try {
			ResponseEntity<SunarpBuscarOficinasResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarOficinasResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_OFICINAS +	ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
			
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_OFICINAS +	ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	@Override
	public SunarpBuscarTitularidadBodyResponse buscarTituaridad(String cuo, String tipoParticipante, String apellidoPaterno,
			String apellidoMaterno, String nombres, String razonSocial) throws Exception {
		
		SunarpBuscarTitularidadBodyResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServiceTitularidadSIRSARP";
		
		Map<String, String> uriVariables = new HashMap<String, String>();
        uriVariables.put("out", ProjectConstants.FormatoRespuesta.JSON);
        uriVariables.put("usuario", ProjectProperties.getServicioSunarpUsuario());
        uriVariables.put("clave", ProjectProperties.getServicioSunarpClave());
        uriVariables.put("tipoParticipante", tipoParticipante.toUpperCase());
        uriVariables.put("apellidoPaterno", apellidoPaterno.toUpperCase());
        uriVariables.put("apellidoMaterno", apellidoMaterno.toUpperCase());
        uriVariables.put("nombres", nombres.toUpperCase());
        uriVariables.put("razonSocial", ProjectUtils.isNullOrEmpty(razonSocial)? razonSocial : razonSocial.toUpperCase().replace("&", "%26"));
        
        String url2 = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", "{out}")
                .queryParam("usuario", "{usuario}")
                .queryParam("clave", "{clave}")
                .queryParam("tipoParticipante", "{tipoParticipante}")
                .queryParam("apellidoPaterno", "{apellidoPaterno}")
                .queryParam("apellidoMaterno", "{apellidoMaterno}")
                .queryParam("nombres", "{nombres}")
                .queryParam("razonSocial", "{razonSocial}")
                .buildAndExpand(uriVariables)
                .toUriString();
       
//		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
//                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
//                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
//                .queryParam("clave", ProjectProperties.getServicioSunarpClave())
//                .queryParam("tipoParticipante", tipoParticipante.toUpperCase())
//                .queryParam("apellidoPaterno", apellidoPaterno.toUpperCase())
//                .queryParam("apellidoMaterno", apellidoMaterno.toUpperCase())
//                .queryParam("nombres", nombres.toUpperCase())
//                .queryParam("razonSocial", ProjectUtils.isNullOrEmpty(razonSocial)? razonSocial : razonSocial.toUpperCase().replace("&", "%26"));
//		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		log.info("{} Se consumio el endpoint : {}", cuo, url2);
		try {
//			ResponseEntity<SunarpBuscarTitularidadBodyResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarTitularidadBodyResponse.class);
			ResponseEntity<SunarpBuscarTitularidadBodyResponse> responseEntity = restTemplate.getForEntity(url2, SunarpBuscarTitularidadBodyResponse.class,uriVariables);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
				//List<SunarpInmuebleDto>
				if(response!=null && response.getBuscarTitularidadSIRSARPResponse() !=null && response.getBuscarTitularidadSIRSARPResponse().getRespuestaTitularidad() != null) {
					if(!(response.getBuscarTitularidadSIRSARPResponse().getRespuestaTitularidad().getTitularidades() instanceof List))
						response.getBuscarTitularidadSIRSARPResponse().getRespuestaTitularidad().setTitularidades(Collections.singletonList(response.getBuscarTitularidadSIRSARPResponse().getRespuestaTitularidad().getTitularidades()));
				}
				
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_TITULARIDAD +ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}

		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_TITULARIDAD+ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	@Override
	public SunarpBuscarAsientosResponse buscarAsientos(String cuo, String registro, String zona, String oficina,
			String partida) throws Exception {
		
		SunarpBuscarAsientosResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServicelistarAsientosSIRSARP";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
                .queryParam("clave", ProjectProperties.getServicioSunarpClave())
                .queryParam("registro", registro)
                .queryParam("zona", zona)
                .queryParam("oficina", oficina)
                .queryParam("partida", partida);

		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ResponseEntity<SunarpBuscarAsientosResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarAsientosResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
				if(response!=null && response.getListarAsientosSIRSARPResponse()!=null && response.getListarAsientosSIRSARPResponse().getAsientos()!=null) {
					if(response.getListarAsientosSIRSARPResponse().getAsientos().getListAsientos()!=null) {
						if(!(response.getListarAsientosSIRSARPResponse().getAsientos().getListAsientos() instanceof List)) {
							List<SunarpAsientoDto> listdto = new ArrayList<>();
							SunarpAsientoDto dto = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListAsientos(), SunarpAsientoDto.class);
							listdto.add(dto);
							response.getListarAsientosSIRSARPResponse().getAsientos().setListAsientos(listdto);
						}else {
							List<SunarpAsientoDto> listdto = new ArrayList<>();
							List<LinkedHashMap<String, Object>> linkedHashMap = (List<LinkedHashMap<String, Object>>) response.getListarAsientosSIRSARPResponse().getAsientos().getListAsientos();
							linkedHashMap.forEach(linked->{
						        SunarpAsientoDto sunarpAsientoDto = objectMapper.convertValue(linked, SunarpAsientoDto.class);
							    listdto.add(sunarpAsientoDto);
							});
						    response.getListarAsientosSIRSARPResponse().getAsientos().setListAsientos(listdto);
						}
						ArrayList<SunarpAsientoDto> ll = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListAsientos(), ArrayList.class);
						ll.forEach(asientos->{
							if(!(asientos.getListPag() instanceof List)) 
								asientos.setListPag(Collections.singletonList(asientos.getListPag()));
						});
					}
					if(response.getListarAsientosSIRSARPResponse().getAsientos().getListFichas()!=null) {
						if(!(response.getListarAsientosSIRSARPResponse().getAsientos().getListFichas() instanceof List)) {
							List<SunarpFichaDto> listdto = new ArrayList<>();
							SunarpFichaDto dto = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListFichas(), SunarpFichaDto.class);
							listdto.add(dto);
							response.getListarAsientosSIRSARPResponse().getAsientos().setListFichas(listdto);
						}else {
							List<SunarpFichaDto> listdto = new ArrayList<>();
							List<LinkedHashMap<String, Object>> linkedHashMap = (List<LinkedHashMap<String, Object>>) response.getListarAsientosSIRSARPResponse().getAsientos().getListFichas();
							linkedHashMap.forEach(linked->{
								SunarpFichaDto sunarpFichaDto = objectMapper.convertValue(linked, SunarpFichaDto.class);
							    listdto.add(sunarpFichaDto);
							});
						    response.getListarAsientosSIRSARPResponse().getAsientos().setListFichas(listdto);
						}
						ArrayList<SunarpFichaDto> ll = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListFichas(), ArrayList.class);
						ll.forEach(fichas->{
							if(!(fichas.getListPag() instanceof List)) 
								fichas.setListPag(Collections.singletonList(fichas.getListPag()));
						});
					}
					if(response.getListarAsientosSIRSARPResponse().getAsientos().getListFolios()!=null) {
						if(!(response.getListarAsientosSIRSARPResponse().getAsientos().getListFolios() instanceof List)) {
							List<SunarpFolioDto> listdto = new ArrayList<>();
							SunarpFolioDto dto = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListFolios(), SunarpFolioDto.class);
							listdto.add(dto);
							response.getListarAsientosSIRSARPResponse().getAsientos().setListFolios(listdto);
						}else {
							List<SunarpFolioDto> listdto = new ArrayList<>();
							List<LinkedHashMap<String, Object>> linkedHashMap = (List<LinkedHashMap<String, Object>>) response.getListarAsientosSIRSARPResponse().getAsientos().getListFolios();
							linkedHashMap.forEach(linked->{
								SunarpFolioDto sunarpFichaDto = objectMapper.convertValue(linked, SunarpFolioDto.class);
							    listdto.add(sunarpFichaDto);
							});
						    response.getListarAsientosSIRSARPResponse().getAsientos().setListFolios(listdto);
						}
//						ArrayList<SunarpFolioDto> ll = objectMapper.convertValue(response.getListarAsientosSIRSARPResponse().getAsientos().getListFolios(), ArrayList.class);
//						ll.forEach(folio->{
//							if(!(folio.getListPag() instanceof List)) 
//								folio.setListPag(Collections.singletonList(folio.getListPag()));
//						});
					}					
				}
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTOS +ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
			
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTOS+ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	@Override
	public SunarpBuscarAsientoImagenResponse buscarAsientoImagen(String cuo, String transaccion, String idImg, String tipo,
			String nroTotalPag, String nroPagRef, String pagina) throws Exception {

		SunarpBuscarAsientoImagenResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServiceverAsientosSIRSARP";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
                .queryParam("clave", ProjectProperties.getServicioSunarpClave())
                .queryParam("transaccion", transaccion)
                .queryParam("idImg", idImg)
                .queryParam("tipo", tipo)
                .queryParam("nroTotalPag", nroTotalPag)
                .queryParam("nroPagRef", nroPagRef)
                .queryParam("pagina", pagina);

		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		try {
			ResponseEntity<SunarpBuscarAsientoImagenResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarAsientoImagenResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTO_IMG +ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
			
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_ASIENTO_IMG+ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	@Override
	public SunarpBuscarVehiculoDetalleResponse buscarVehiculoDetalle(String cuo, String zona, String oficina,
			String placa) throws Exception {
		SunarpBuscarVehiculoDetalleResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServiceverDetalleRPVExtra";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
                .queryParam("clave", ProjectProperties.getServicioSunarpClave())
                .queryParam("zona", zona)
                .queryParam("oficina", oficina)
                .queryParam("placa", placa);

		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		try {
			
			ResponseEntity<SunarpBuscarVehiculoDetalleResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarVehiculoDetalleResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
				if(response!=null && response.getVerDetalleRPVExtraResponse()!=null && response.getVerDetalleRPVExtraResponse().getVehiculo()!=null && response.getVerDetalleRPVExtraResponse().getVehiculo().getPropietarios()!=null && response.getVerDetalleRPVExtraResponse().getVehiculo().getPropietarios().getNombre()!=null) {
					if(!(response.getVerDetalleRPVExtraResponse().getVehiculo().getPropietarios().getNombre() instanceof List))
						response.getVerDetalleRPVExtraResponse().getVehiculo().getPropietarios().setNombre(Collections.singletonList(response.getVerDetalleRPVExtraResponse().getVehiculo().getPropietarios().getNombre().toString()));
				}
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_VEHICULO_DETALLE +ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_VEHICULO_DETALLE+ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	@Override
	public SunarpBuscarPersonaJuridicaResponse buscarPersonaJuridica(String cuo, String razonSocial) throws Exception {
		SunarpBuscarPersonaJuridicaResponse response = null;
		String url = ProjectProperties.getServicioSunarpUrl() + "/WSServicePJRazonSocial";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("usuario", ProjectProperties.getServicioSunarpUsuario())
                .queryParam("clave", ProjectProperties.getServicioSunarpClave())
                .queryParam("razonSocial", razonSocial);

		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.buildAndExpand().toUri());
		try {
			ResponseEntity<SunarpBuscarPersonaJuridicaResponse> responseEntity = restTemplate.getForEntity(builderUrl.buildAndExpand().toUri(), SunarpBuscarPersonaJuridicaResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SUNARP_BUSCAR_PERSONA_JURIDICA+ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
			
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR+ProjectConstants.Proceso.SUNARP_BUSCAR_PERSONA_JURIDICA+ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

	
	
}
