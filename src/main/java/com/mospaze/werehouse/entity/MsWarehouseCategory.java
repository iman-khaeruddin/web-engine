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
@Table(name = "master_warehouse_category")
public class MsWarehouseCategory implements Serializable{

	private static final long serialVersionUID = 1551581175919689302L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotBlank
	@Column(name = "category_name")
	private String category_name;
	
	@Column(name = "status")
	private int status;

	@Column(name = "created_at")
	private Date created_at;

	@Column(name = "updated_at")
	private Date updated_at;

	@Override
	public String toString() {
		return "MsWarehouseCategory{" +
				"id=" + id +
				", category_name='" + category_name + '\'' +
				", status=" + status +
				", created_at=" + created_at +
				", updated_at=" + updated_at +
				'}';
	}
}
