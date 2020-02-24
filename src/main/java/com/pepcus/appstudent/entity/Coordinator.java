package com.pepcus.appstudent.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Table(name="coordinator")
@Data
public class Coordinator {
@Id
@GeneratedValue(strategy=GenerationType.AUTO)
@Column(name = "id")
private Integer id;

@Column(name="first_name")
private String firstName;

@Column(name="last_name")
private String lastName;

@Column(name="gender")
private String gender;

@Column(name="primary_contact_number")
private String primaryContactNumber;

@Column(name="is_primary_number_on_whatsapp")
private Boolean isPrimaryNumberOnWhatsapp;

@Column(name="alternate_contact_number")
private String alternateContactNumber;

@Column(name="email")
private String email;

@Column(name="dob")
private String dob;

@Column(name="address")
private String address;

@Column(name = "area")
private String area;

@Column(name = "intrested_departments")
private String intrestedDepartment;

@Column(name = "assigned_departments")
private String assignedDepartment;

@Transient
List<String> intrestedDepartments;

@Transient
List<String> assignedDepartments;

@Column(name = "remarks")
private String remarks;

@Column(name="bus_number")
private Integer busNumber;

@Column(name="class_number")
private Integer classNumber;

@Column(name="isActive")
private Boolean isActive;

}


