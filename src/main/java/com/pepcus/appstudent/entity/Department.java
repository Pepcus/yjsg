package com.pepcus.appstudent.entity;

import java.io.Serializable;
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "department")
@ToString(exclude = {"departmentValues"})
@EqualsAndHashCode(exclude = { "departmentValues"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
public class Department implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "internal_name")
	private String internalName;

	@Column(name = "display_name")
	private String displayName;
	
	@Column(name = "department_value_type")
	private String departmentValueType;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "department", cascade = CascadeType.REMOVE, orphanRemoval = false)
	private Set<DepartmentValue> departmentValues;
}
