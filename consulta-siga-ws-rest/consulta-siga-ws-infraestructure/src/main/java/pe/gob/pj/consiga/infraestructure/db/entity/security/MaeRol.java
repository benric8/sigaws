package pe.gob.pj.consiga.infraestructure.db.entity.security;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.gob.pj.consiga.domain.utils.SecurityConstants;
import pe.gob.pj.consiga.infraestructure.db.entity.Auditoria;

import java.util.List;


/**
 * The persistent class for the mae_rol database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name="mae_rol", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
@NamedQuery(name="MaeRol.findAll", query="SELECT m FROM MaeRol m")
@NamedQuery(name=MaeRol.FIND_ROLES_BY_ID_USUARIO, query="SELECT m FROM MaeRol m JOIN m.maeRolUsuarios ur WHERE m.activo = '1' AND ur.maeUsuario.nUsuario = :idUsuario")
public class MaeRol extends Auditoria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String FIND_ROLES_BY_ID_USUARIO = "MaeRol.rolesPorUsuario";

	@Id
	@Column(name="n_rol")
	private Integer nRol;

	@Column(name="c_rol")
	private String cRol;

	@Column(name="x_descripcion")
	private String xDescripcion;

	@Column(name="x_rol")
	private String xRol;

	//bi-directional many-to-one association to MaeOperacion
	@OneToMany(mappedBy="maeRol")
	private List<MaeOperacion> maeOperacions;

	//bi-directional many-to-one association to MaeRolUsuario
	@OneToMany(mappedBy="maeRol")
	private List<MaeRolUsuario> maeRolUsuarios;

}