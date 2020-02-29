package com.mospaze.werehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "master_warehouse_features")
public class MsWarehouseFeature implements Serializable{

	private static final long serialVersionUID = -1081992063365087505L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	@Column(name = "features_name")
	private String features_name;

	@Column(name = "icon")
	private String icon;
	
	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	private Date created_at;

	@Column(name = "updated_at")
	private Date updated_at;

	@Override
	public String toString() {
		return "MsWarehouseFeature{" +
				"id=" + id +
				", features_name='" + features_name + '\'' +
				", icon='" + icon + '\'' +
				", status=" + status +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				'}';
	}
}
