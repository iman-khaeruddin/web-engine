package com.mospaze.werehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "master_warehouse_service")
public class MsWarehouseService implements Serializable{

	private static final long serialVersionUID = -784002306081387509L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	@Column(name = "services_name")
	private String services_name;

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
		return "MsWarehouseService{" +
				"id=" + id +
				", services_name='" + services_name + '\'' +
				", icon='" + icon + '\'' +
				", status=" + status +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				'}';
	}
}
