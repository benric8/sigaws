package pe.gob.pj.consiga.infraestructure.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SbsConsultarTipoCambioCodMonedaBodyResponse {

    @JsonProperty("valor_compra")
    private String valorCompra;
    @JsonProperty("valor_venta")
    private String valorVenta;
    @JsonProperty("codigo_moneda")
    private String codigoMoneda;
    @JsonProperty("descripcion_moneda")
    private String descripcionMoneda;
}
