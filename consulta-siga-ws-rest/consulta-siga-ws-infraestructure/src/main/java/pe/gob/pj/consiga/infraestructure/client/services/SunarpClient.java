package pe.gob.pj.consiga.infraestructure.client.services;

import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientoImagenResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarAsientosResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarOficinasResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarPersonaJuridicaResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarTitularidadBodyResponse;
import pe.gob.pj.consiga.infraestructure.client.response.SunarpBuscarVehiculoDetalleResponse;

public interface SunarpClient {
	public SunarpBuscarOficinasResponse buscarOficinas(String cuo) throws Exception;
	public SunarpBuscarTitularidadBodyResponse buscarTituaridad(String cuo,String tipoParticipante,String apellidoPaterno,String apellidoMaterno,String nombres, String razonSocial) throws Exception;
	public SunarpBuscarAsientosResponse buscarAsientos(String cuo,String registro,String zona,String oficina,String partida) throws Exception;
	public SunarpBuscarAsientoImagenResponse buscarAsientoImagen(String cuo,String transaccion,String idImg,String tipo,String nroTotalPag,String nroPagRef,String pagina) throws Exception;
	public SunarpBuscarVehiculoDetalleResponse buscarVehiculoDetalle(String cuo,String zona,String oficina,String placa) throws Exception;
	public SunarpBuscarPersonaJuridicaResponse buscarPersonaJuridica(String cuo,String razonSocial) throws Exception;
}
