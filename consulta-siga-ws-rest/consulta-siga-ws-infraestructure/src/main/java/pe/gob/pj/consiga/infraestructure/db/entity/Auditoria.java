package pe.gob.pj.consiga.infraestructure.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import pe.gob.pj.consiga.domain.utils.ProjectConstants;
import pe.gob.pj.consiga.domain.utils.ProjectUtils;

@Getter @Setter
@MappedSuperclass
public abstract class Auditoria implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="F_AUD")
	private Date fAud = new Date();
	@Column(name="B_AUD")
	private String bAud = ProjectConstants.Auditoria.B_INSTER;
	@Column(name="C_AUD_UID")
	private String cAudId;
	@Column(name="C_AUD_UIDRED")
	private String cAudIdRed;
	@Column(name="C_AUD_PC")
	private String cAudPc = ProjectUtils.getPc();
	@Column(name="C_AUD_IP")
	private String cAudIp = ProjectUtils.getIp();
	@Column(name="C_AUD_MCADDR")
	private String cAudMcAddr = ProjectUtils.getMac();
	
	@Column(name = "L_ACTIVO", length = 1, nullable = false)
	private String activo = ProjectConstants.Estado.ACTIVO;
//	@Temporal(TemporalType.TIMESTAMP)
//	@Column(name="F_REGISTRO", nullable = false)
//	private Date fechaRegistro = new Date();
}
