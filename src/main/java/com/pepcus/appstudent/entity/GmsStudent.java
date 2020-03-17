package com.pepcus.appstudent.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * Entity class used to map table in DB
 * 
 * @author Sandeep Vishwakarma
 * @since 05-03-2020
 *
 */
@Entity
@Table(name = "student_gms")
@Data
@JsonInclude(Include.NON_EMPTY)
public class GmsStudent {

	public GmsStudent() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "age")
	private Integer age;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;

	@Column(name = "is_whatsapp")
	private String isWhatsApp;

	@Column(name = "status")
	private String registrationStatus;

	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "food_opt")
	private String foodOpt;

	@Column(name = "created_date")
	@JsonIgnore
	private Date dateCreatedInDB;

	@Column(name = "last_modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date dateLastModifiedInDB;

	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String createdDate;

	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String lastModifiedDate;

}