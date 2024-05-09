package pe.gob.pj.consiga.infraestructure.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pe.gob.pj.consiga.domain.port.usecase.SeguridadUseCasePort;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectProperties;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;
import pe.gob.pj.consiga.domain.utils.SecurityConstants;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	@Getter @Setter
	private SeguridadUseCasePort seguridadService;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, SeguridadUseCasePort servicio) {
		super(authenticationManager);
		this.setSeguridadService(servicio);
	}

	/**
	* Descripción : filtra las peticiones HTTP y evalua el token
	* @param HttpServletRequest request - peticion HTTP
	* @param HttpServletResponse response, - respuesta HTTP    
	* @param FilterChain filterChain - cadenas filtro 
	* @return void 
	* @exception Captura excepcion generica
	*/
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		request.setAttribute(ProjectConstants.AUD_CUO, ProjectUtils.obtenerCodigoUnico());
		request.setAttribute(ProjectConstants.AUD_IP, !ProjectUtils.isNullOrEmpty(request.getRemoteAddr()) ? request.getRemoteAddr() : request.getRemoteHost());
		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		if (authentication == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			filterChain.doFilter(request, response);
			return;
		}
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}

	/**
	* Descripción : obtiene la autenticacion desde token
	* @param HttpServletRequest request - peticion HTTP
	* @return UsernamePasswordAuthenticationToken - Informacion de autenticacion proveniente token 
	* @exception Captura excepcion generica
	*/
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		String urlReq = request.getRequestURI();
		String metodo = request.getMethod();
		if(metodo.equalsIgnoreCase(ProjectConstants.METHOD_CORTA_ULTIMA_BARRA_INVERTIDA)) {
			urlReq = urlReq.substring(0, urlReq.lastIndexOf("/"));//corta el id que se manda en la url
		}
		String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
		String remoteIp = request.getAttribute(ProjectConstants.AUD_IP).toString();
		String cuo = request.getAttribute(ProjectConstants.AUD_CUO).toString();
		byte[] signingKey = SecurityConstants.JWT_SECRET.getBytes();
		if (!ProjectUtils.isNullOrEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			try {
				String jwt = token.replace("Bearer ", "");
				request.setAttribute(ProjectConstants.AUD_JWT, jwt);
				Jws<Claims> parsedToken = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt);
				
				String accesoBase = (String) parsedToken.getBody().get(ProjectConstants.Claim.ACCESO);
				String username = parsedToken.getBody().getSubject();
				
				@SuppressWarnings("unchecked")
				List<String> roles = (List<String>) parsedToken.getBody().get(ProjectConstants.Claim.ROL);

				List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody().get(ProjectConstants.Claim.ROL)).stream()
						.map(authority -> new SimpleGrantedAuthority((String) authority)).collect(Collectors.toList());
				
				String ipRemotaDeToken = parsedToken.getBody().get(ProjectConstants.Claim.IP).toString();
				
				int tiempoSegundosRefresh = ProjectProperties.getSeguridadTiempoRefreshSegundos();
				
				Date ahora = new Date();
				Date limiteExpira = parsedToken.getBody().getExpiration();
				Date limiteRefresh = ProjectUtils.sumarRestarSegundos(limiteExpira, tiempoSegundosRefresh);

				String tieneAcceso = "";
				if (!urlReq.endsWith("refresh")) {
					tieneAcceso = seguridadService.validarAccesoMetodo(cuo, username, roles.get(0), urlReq);
					if (ProjectUtils.isNullOrEmpty(tieneAcceso)) {
						log.info("{} El usuario [{}], no tiene acceso al método [{}] ", cuo, username, urlReq);
						return null;
					}
				}

				if (!remoteIp.equals(ipRemotaDeToken) || ahora.after(limiteRefresh)) {
                	logger.error(cuo+" hubo problema con la ip remoto o limite de expiración: "+token);
					return null;
				}
				String accesoOperacion = !tieneAcceso.contains(ProjectConstants.TOKEN_OPERACION_SPLIT) ? ProjectConstants.TOKEN_ACCESO_INTERNO : tieneAcceso.split(ProjectConstants.TOKEN_OPERACION_SPLIT)[1];
				if(!accesoOperacion.equalsIgnoreCase(ProjectConstants.TOKEN_ACCESO_NEUTRO)) {
					if(!urlReq.endsWith("refresh") && !accesoOperacion.equalsIgnoreCase(accesoBase)) {
						log.info("{} El token no tiene permiso [{}] para el método [{}]. ", cuo, ProjectConstants.TOKEN_ACCESO_INTERNO, urlReq);
						return null;
					}
				}

				if (!ProjectUtils.isNullOrEmpty(username)) {
					return new UsernamePasswordAuthenticationToken(username, null, authorities);
				}
				
			} catch (ExpiredJwtException exception) {
				String ipRemotaToken = exception.getClaims().get(ProjectConstants.Claim.IP).toString();
				String subject = exception.getClaims().getSubject();
				int total = (int) exception.getClaims().get(ProjectConstants.Claim.NUMERO);			
				
				if (urlReq.endsWith("refresh") && remoteIp.equals(ipRemotaToken) && total<=ProjectConstants.NRO_VECES_REFRESH_CON_TOKEN_EXPIRADO) { // && !ahora.after(limiteRefresh)
					List<SimpleGrantedAuthority> authorities = ((List<?>) exception.getClaims().get(ProjectConstants.Claim.ROL)).stream()
							.map(authority -> new SimpleGrantedAuthority((String) authority))
							.collect(Collectors.toList());
					++total;
					return new UsernamePasswordAuthenticationToken(subject, null, authorities);
				}
				log.warn(cuo + "Request to parse expired JWT : {} failed : {}", token, exception.getMessage());
			} catch (UnsupportedJwtException exception) {
				log.warn(cuo + "Request to parse unsupported JWT : {} failed : {}", token, exception.getMessage());
			} catch (MalformedJwtException exception) {
				log.warn(cuo + "Request to parse invalid JWT : {} failed : {}", token, exception.getMessage());
			} catch (SignatureException exception) {
				log.warn(cuo + "Request to parse JWT with invalid signature : {} failed : {}", token,
						exception.getMessage());
			} catch (IllegalArgumentException exception) {
				log.warn(cuo + "Request to parse empty or null JWT : {} failed : {}", token, exception.getMessage());
			} catch (Exception e) {
				log.error(cuo + "No se obtubo owner y código de BASE DE DATOS: " + ProjectUtils.convertExceptionToString(e));
				e.printStackTrace();
			}
		}
		if(!urlReq.endsWith("healthcheck")) {
			log.error(cuo + "Hubo un problema con el token : " + token);
		}
		return null;
	}

}