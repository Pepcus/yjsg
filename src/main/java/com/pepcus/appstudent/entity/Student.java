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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * Entity class used to map table in DB
 * 
 * @author Shubham Solanki
 * @since 12-02-2018
 *
 */
@Entity
@Table(name = "student")
@Data
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "father_name")
	private String fatherName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "age")
	private String age;

	@Column(name = "education")
	private String education;

	@Column(name = "occupation")
	private String occupation;

	@Column(name = "mother_mobile")
	private String motherMobile;

	@Column(name = "father_mobile")
	private String fatherMobile;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Column(name = "bus_stop")
	private String busStop;

	@Column(name = "class_attended_2016")
	private String classAttended2016;

	@Column(name = "class_room_no_2016")
	private String classRoomNo2016;

	@Column(name = "attendance_2016")
	private String attendance2016;

	@Column(name = "marks_2016")
	private String marks2016;

	@Column(name = "class_attended_2017")
	private String classAttended2017;

	@Column(name = "class_room_no_2017")
	private String classRoomNo2017;

	@Column(name = "attendance_2017")
	private String attendance2017;

	@Column(name = "marks_2017")
	private String marks2017;

	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonProperty(access = Access.READ_ONLY)
	private Date createdDate;

	@Column(name = "last_modified_date")
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonProperty(access = Access.READ_ONLY)
	private Date lastModifiedDate;

}