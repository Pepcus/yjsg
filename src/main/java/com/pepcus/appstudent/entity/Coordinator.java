package com.pepcus.appstudent.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "coordinator")
@ToString
@EqualsAndHashCode(exclude = { "interestedDepartments", "assignedDepartments" })
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class Coordinator implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "dob")
	private String dob;

	@Column(name = "gender")
	private String gender;

	@Column(name = "email")
	private String email;

	@Column(name = "whatsapp_number")
	private String whatsappNumber;

	@Column(name = "alternate_number")
	private String alternateNumber;

	@Column(name = "address")
	private String address;

	@Column(name = "area")
	private String area;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "secret_key")
	private String secretKey;

	@Column(name = "is_active")
	private String isActive;

	@Column(name = "created_date")
	@JsonIgnore
	private Date dateCreatedInDB;

	@Column(name = "last_modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonIgnore
	private Date dateLastModifiedInDB;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coordinator", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private Set<CoordinatorInterestedDepartment> interestedDepartments;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coordinator", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private Set<CoordinatorAssignedDepartment> assignedDepartments;
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String createdDate;

	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String lastModifiedDate;

}
