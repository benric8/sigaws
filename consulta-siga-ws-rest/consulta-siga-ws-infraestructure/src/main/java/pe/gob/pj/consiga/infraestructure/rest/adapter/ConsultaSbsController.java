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
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.infraestructure.rest.response.GlobalResponse;

import pe.gob.pj.consiga.infraestructure.client.response.SbsConsultarTipoCambioCodMonedaBodyResponse;
import pe.gob.pj.consiga.infraestructure.client.services.SbsClient;

@Slf4j
@RestController
@RequestMapping(value="sbs", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class ConsultaSbsController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    @Qualifier("sbsClient")
    private SbsClient client;

    @GetMapping(value="consultar/tipoCambioPorFechaYCodMoneda")
    public ResponseEntity<GlobalResponse> consultarTipoCambioPorFechaYCodMoneda(@RequestAttribute(name= ProjectConstants.AUD_CUO) String cuo,
                                                                                @Pattern(regexp = ProjectConstants.Pattern.FECHA_SBS, message = "El parámetro fecha no tiene un formato válido (DDMMAAAA).")
                                                                                @NotBlank(message = "El parámetro fecha no puede tener un valor vacío.")
                                                                                @RequestParam(name = "fecha", required = true)
                                                                                String fecha,
                                                                                @Pattern(regexp = ProjectConstants.Pattern.NUMBER, message = "El parámetro codigoMoneda no tiene un formato válido.")
                                                                                @NotBlank(message = "El parámetro codigoMoneda no puede ser vacío.")
                                                                                @Length(min = 1, max = 3, message = "El parámetro codigoMoneda no tiene un tamaño válido [min=1,max=3].")
                                                                                @RequestParam(name="codigoMoneda", required = true)
                                                                                String codMoneda,
                                                                                @RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta){
        GlobalResponse res = new GlobalResponse();
        res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));

        try {
            SbsConsultarTipoCambioCodMonedaBodyResponse data = client.consultarTipoCambioPorFechaYCodMoneda(cuo, fecha, codMoneda);
            if (data==null) {
                throw new ErrorException(ProjectConstants.Error.C0001,
                        ProjectConstants.Error.X0001 + ProjectConstants.Proceso.SBS_CONSULTAR_TIPOCAMBIO_POR_CODIGO_MONEDA,null, null);
            }
            res.setCodigo(ProjectConstants.Error.CEXITO);
            res.setDescripcion(ProjectConstants.Error.XEXITO);
            res.setData(data);
        } catch (ErrorException e) {
            handleException(cuo, e, res);
        } catch (Exception e) {
            handleException(cuo, new ErrorException(ProjectConstants.Error.CE000,
                    ProjectConstants.Error.XERROR + ProjectConstants.Proceso.CONADIS_BUSCAR_PERSONA_DISCAPACIDAD + ProjectConstants.Error.XE000,
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
