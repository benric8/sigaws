package pe.gob.pj.consiga.infraestructure.client.services;

import pe.gob.pj.consiga.infraestructure.client.response.SbsConsultarTipoCambioCodMonedaBodyResponse;

public interface SbsClient {
    public SbsConsultarTipoCambioCodMonedaBodyResponse consultarTipoCambioPorFechaYCodMoneda(String cuo, String fecha, String codMoneda) throws Exception;
}
