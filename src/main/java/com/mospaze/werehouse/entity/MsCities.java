package com.mospaze.werehouse.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "master_location_cities")
public class MsCities implements Serializable{

	private static final long serialVersionUID = -2725324516710104469L;
	
	@Id
	@Column(name = "id")
	private int id;
	
	@Column(name = "state_id")
	private int state_id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "latitude")
	private double latitude;
	
	@Column(name = "longitude")
	private double longitude;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date created_at;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updated_at;
}
