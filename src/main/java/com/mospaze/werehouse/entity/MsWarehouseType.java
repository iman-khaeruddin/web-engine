package com.mospaze.werehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "master_warehouse_type")
public class MsWarehouseType implements Serializable{

	private static final long serialVersionUID = -5074216172081121965L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	@Column(name = "type_name")
	private String type_name;
	
	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	private Date created_at;

	@Column(name = "updated_at")
	private Date updated_at;

	@Override
	public String toString() {
		return "MsWarehouseType{" +
				"id=" + id +
				", type_name='" + type_name + '\'' +
				", status=" + status +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				'}';
	}
}
