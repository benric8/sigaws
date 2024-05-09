package pe.gob.pj.consiga.infraestructure.client.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.exceptions.ErrorException;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.infraestructure.client.response.ConadisBuscarPesonaBodyResponse;

@Slf4j
@Service("conadisClient")
public class ConadisClientImpl implements ConadisClient{

	@Autowired
    private RestTemplate restTemplate;
	
	@Override
	public ConadisBuscarPesonaBodyResponse buscarPersonaDiscapacidad(String cuo, String numeroDocumentoIdentidad)
			throws Exception {
		ConadisBuscarPesonaBodyResponse response = null;
		String url = ProjectProperties.getServicioConadisUrl() + "/PDiscapacidad";
		
		UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON)
                .queryParam("Username", ProjectProperties.getServicioConadisUsuario())
                .queryParam("Password", ProjectProperties.getServicioConadisClave())
        		.queryParam("DocNumber", numeroDocumentoIdentidad);
		log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.toUriString());
		try {
			ResponseEntity<ConadisBuscarPesonaBodyResponse> responseEntity = restTemplate.getForEntity(builderUrl.toUriString(), ConadisBuscarPesonaBodyResponse.class);
			if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
				response = responseEntity.getBody();
			} else {
				throw new ErrorException(ProjectConstants.Error.CE013, 
						ProjectConstants.Error.XERROR + ProjectConstants.Proceso.CONADIS_BUSCAR_PERSONA_DISCAPACIDAD +	ProjectConstants.Error.XE013 + url,
						"endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
			}
			
		} catch (Exception e) {
			throw new ErrorException(ProjectConstants.Error.CE013, 
					ProjectConstants.Error.XERROR + ProjectConstants.Proceso.CONADIS_BUSCAR_PERSONA_DISCAPACIDAD +	ProjectConstants.Error.XE013 + url,
					e.getMessage(), e);
		}
		return response;
	}

}
