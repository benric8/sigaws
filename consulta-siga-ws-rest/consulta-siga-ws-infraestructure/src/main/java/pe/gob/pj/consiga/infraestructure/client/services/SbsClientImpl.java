package pe.gob.pj.consiga.infraestructure.client.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pe.gob.pj.consiga.domain.exceptions.ErrorException;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.infraestructure.client.response.ConadisBuscarPesonaBodyResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SbsConsultarTipoCambioCodMonedaBodyResponse;

@Slf4j
@Service("sbsClient")
public class SbsClientImpl implements SbsClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SbsConsultarTipoCambioCodMonedaBodyResponse consultarTipoCambioPorFechaYCodMoneda(String cuo, String fecha, String codMoneda) throws Exception {

        SbsConsultarTipoCambioCodMonedaBodyResponse response = null;
        String url = ProjectProperties.getServicioSbsUrl () + "/PromedioCodigo";

        UriComponentsBuilder builderUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("fecha", fecha)
                .queryParam("cod", codMoneda)
                .queryParam("out", ProjectConstants.FormatoRespuesta.JSON);
        log.info("{} Se consumio el endpoint : {}", cuo, builderUrl.toUriString());
        try {
            ResponseEntity<SbsConsultarTipoCambioCodMonedaBodyResponse> responseEntity = restTemplate.getForEntity(builderUrl.toUriString(), SbsConsultarTipoCambioCodMonedaBodyResponse.class);
            if (responseEntity.getStatusCodeValue() == HttpStatus.OK.value()) {
                    response = responseEntity.getBody();
            } else {
                throw new ErrorException(ProjectConstants.Error.CE013,
                        ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SBS_CONSULTAR_TIPOCAMBIO_POR_CODIGO_MONEDA +	ProjectConstants.Error.XE013 + url,
                        "endpoint retorno status "+responseEntity.getStatusCodeValue(), null);
            }

        } catch (Exception e) {
            throw new ErrorException(ProjectConstants.Error.CE013,
                    ProjectConstants.Error.XERROR + ProjectConstants.Proceso.SBS_CONSULTAR_TIPOCAMBIO_POR_CODIGO_MONEDA +	ProjectConstants.Error.XE013 + url,
                    e.getMessage(), e);
        }
        return response;
    }
}
