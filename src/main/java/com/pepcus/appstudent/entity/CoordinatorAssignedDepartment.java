package com.pepcus.appstudent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Table(name = "coordinator_assigned_departments", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "department_id", "coordinator_id" }) })
@Data
public class CoordinatorAssignedDepartment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_coordinator_assigned_departments_department"))
	private Department department;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coordinator_id", foreignKey = @ForeignKey(name = "fk_coordinator_assigned_departments_department"))
	private Coordinator coordinator;

	@Column(name = "value")
	private String value;
	
	@Transient
	private String internalName;

	@Transient
	private String displayName;
}
