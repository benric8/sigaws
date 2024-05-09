package pe.gob.pj.consiga.infraestructure.rest.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.domain.utils.SecurityConstants;
import pe.gob.pj.consiga.infraestructure.rest.response.GlobalResponse;

@Slf4j
@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class DefaultController implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Método que sirve para verificar versión actual del aplicativo
	 * 
	 * @param cuo Código único de log
	 * @return Datos del aplicativo
	 */
	@GetMapping(value = "/healthcheck")
	public ResponseEntity<Object> healthcheck(@RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo,
			@RequestParam(name = "formatoRespuesta", defaultValue = "json", required = false) String formatoRespuesta) {
		GlobalResponse res = new GlobalResponse();
		try {
			res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
			res.setCodigo(ProjectConstants.Error.CEXITO);
			res.setDescripcion("Versión actual de aplicativo");
			Map<String, String> healthcheck = new HashMap<String, String>();
			healthcheck.put("Aplicativo", ProjectConstants.Aplicativo.NOMBRE);
			healthcheck.put("Estado", "Disponible");
			healthcheck.put("Versión", ProjectConstants.Aplicativo.VERSION);
			res.setData(healthcheck);
		} catch (Exception e) {
			res.setCodigo("E500");
			res.setDescripcion(ProjectUtils.isNull(e.getCause()).concat(e.getMessage()));
			log.error("{} {} | {} | {} | {} | {}", cuo, ProjectConstants.Error.TRAZA_LOG, res.getCodigo(), res.getDescripcion(), ProjectUtils.obtenerClaseMetodoLineaException(e), ProjectUtils.obtenerCausaException(e));
		}
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.parseMediaType(ProjectConstants.FormatoRespuesta.JSON.equalsIgnoreCase(formatoRespuesta) ? MediaType.APPLICATION_JSON_VALUE : MediaType.APPLICATION_XML_VALUE));
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
		
	}


	/**
	 * MÉTODO QUE GENERA NUEVO TOKEN A PARTIR DE TOKEN ANTERIOR
	 * 
	 * @param token           es token antentior
	 * @param ipRemota        es la ip desde donde lo solicita
	 * @param tokenAdmin      es el token de la seccion administrador
	 * @param validTokenAdmin indicador si necesitamos validar token del admin
	 * @param cuo             código único de log
	 * @return un nuevo token
	 */
	@SuppressWarnings("unchecked")
	@GetMapping(value = "/seguridad/refresh")
	public ResponseEntity<GlobalResponse> refreshToken(@RequestAttribute(name = ProjectConstants.AUD_CUO) String cuo, 
			@RequestAttribute(name=ProjectConstants.AUD_IP) String ipRemota,
			@RequestParam(required = true) String token) {
		GlobalResponse res = new GlobalResponse();
		res.setCodigoOperacion(cuo.substring(1, cuo.length()-1));
		try {			
			byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();

			Map<String, String> dataToken = new HashMap<String, String>();
			try {
				String jwt = token.replace("Bearer ", "");
				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt);
				String accesoBase =  (String) parsedToken.getBody().get(ProjectConstants.Claim.ACCESO);
				List<String> roles = (List<String>) parsedToken.getBody().get(ProjectConstants.Claim.ROL);
				String ipRemotaToken = parsedToken.getBody().get(ProjectConstants.Claim.IP).toString();
				int total = (int) parsedToken.getBody().get(ProjectConstants.Claim.NUMERO);
				String subject = parsedToken.getBody().getSubject();
				
				Date ahora = new Date();
				
				int tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
				int tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();
				
				Date limiteExpira = parsedToken.getBody().getExpiration();
				Date limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);
				
				if (ipRemota.equals(ipRemotaToken)) {
					if(!ahora.after(limiteRefresh)) {
							String tokenResult = Jwts.builder()
								.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
								.setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
								.setIssuer(SecurityConstants.TOKEN_ISSUER)
								.setAudience(SecurityConstants.TOKEN_AUDIENCE)
								.setSubject(subject).setExpiration(ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira))
								.claim(ProjectConstants.Claim.ROL, roles)
								.claim(ProjectConstants.Claim.IP, ipRemota)
								.claim(ProjectConstants.Claim.ACCESO, accesoBase)
								.claim(ProjectConstants.Claim.LIMIT, ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira+tiempoSegundosRefresh))
								.claim(ProjectConstants.Claim.NUMERO, total + 1)
								.compact();
							
						res.setCodigo(ProjectConstants.Error.CEXITO);
						res.setDescripcion(ProjectConstants.Error.XEXITO);
						dataToken.put("token", tokenResult);
						res.setData(dataToken);
						return new ResponseEntity<>(res, HttpStatus.OK);
					}else {
						res.setCodigo(ProjectConstants.Error.CE003);
						res.setDescripcion(ProjectConstants.Error.XE003);
						return new ResponseEntity<>(res, HttpStatus.OK);
					}				
				} else {
					return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
				}
			} catch (ExpiredJwtException e) {
				String accesoBase =  (String) e.getClaims().get(ProjectConstants.Claim.ACCESO);
				List<String> roles = (List<String>) e.getClaims().get(ProjectConstants.Claim.ROL);
				String ipRemotaToken = e.getClaims().get(ProjectConstants.Claim.IP).toString();
				int total = (int) e.getClaims().get(ProjectConstants.Claim.NUMERO);
				String subject = e.getClaims().getSubject();

				Date ahora = new Date();
				
				int tiempoSegundosExpira = ProjectProperties.getSeguridadTiempoExpiraSegundos();
				int tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();
				
				Date limiteExpira = e.getClaims().getExpiration();
				Date limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);
				
				if (ipRemota.equals(ipRemotaToken)) {
					if(!ahora.after(limiteRefresh)) {
						String tokenResult = Jwts.builder()
								.signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
								.setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
								.setIssuer(SecurityConstants.TOKEN_ISSUER)
								.setAudience(SecurityConstants.TOKEN_AUDIENCE)
								.setSubject(subject).setExpiration(ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira))
								.claim(ProjectConstants.Claim.ROL, roles)
								.claim(ProjectConstants.Claim.IP, ipRemota)
								.claim(ProjectConstants.Claim.ACCESO, accesoBase)
								.claim(ProjectConstants.Claim.LIMIT, ProjectUtils.sumarRestarSegundos(ahora, tiempoSegundosExpira+tiempoSegundosRefresh))
								.claim(ProjectConstants.Claim.NUMERO, total + 1)
								.compact();
						res.setCodigo(ProjectConstants.Error.CEXITO);
						res.setDescripcion(ProjectConstants.Error.XEXITO);
						dataToken.put("token", tokenResult);
						res.setData(dataToken);
						return new ResponseEntity<>(res, HttpStatus.OK);
					}else {
						res.setCodigo(ProjectConstants.Error.CE003);
						res.setDescripcion(ProjectConstants.Error.XE003);
						return new ResponseEntity<>(res, HttpStatus.OK);
					}
				} else {
					log.warn(
							"{} No se ha encontrado coincidencias válidas del token anterior o se ha excedido el tiempo limite para refrescar token.",
							cuo);
					return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
				}
			}
		} catch (Exception e) {
			res.setCodigo(ProjectConstants.Error.CE002);
			res.setDescripcion(ProjectConstants.Error.XE002);
			log.error("{} error al intentar generar nuevo Token: {}", cuo, ProjectUtils.isNull(e.getCause()).concat(e.getMessage()));
		}
		return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
	}

}
