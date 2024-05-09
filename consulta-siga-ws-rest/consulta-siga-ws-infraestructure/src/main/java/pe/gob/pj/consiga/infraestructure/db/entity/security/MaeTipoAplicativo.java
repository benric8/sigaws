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
 * The persistent class for the mae_tipo_aplicativo database table.
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name="mae_tipo_aplicativo", schema = SecurityConstants.ESQUEMA_SEGURIDAD)
@NamedQuery(name="MaeTipoAplicativo.findAll", query="SELECT m FROM MaeTipoAplicativo m")
public class MaeTipoAplicativo extends Auditoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="n_tipo_aplicativo")
	private Integer nTipoAplicativo;

	@Column(name="x_descripcion")
	private String xDescripcion;

	@Column(name="x_tipo_aplicativo")
	private String xTipoAplicativo;

	//bi-directional many-to-one association to MaeAplicativo
	@OneToMany(mappedBy="maeTipoAplicativo")
	private List<MaeAplicativo> maeAplicativos;

}