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

import org.springframework.core.annotation.Order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
@JsonInclude(Include.ALWAYS)
public class Student {

	public Student(){
		
	}
	public Student(Integer id, String name, String fatherName) {
		super();
		this.id = id;
		this.name = name;
		this.fatherName = fatherName;
	}

	@Order(value=1)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Order(value=2)
	@Column(name = "name")
	private String name;

	@Order(value=3)
	@Column(name = "father_name")
	private String fatherName;

	@Order(value=4)
	@Column(name = "gender")
	private String gender;

	@Order(value=5)
	@Column(name = "age")
	private String age;

	@Order(value=6)
	@Column(name = "education")
	private String education;

	@Order(value=7)
	@Column(name = "occupation")
	private String occupation;

	@Column(name = "mother_mobile")
	private String motherMobile;

	@Column(name = "father_mobile")
	private String mobile;

	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address	;

	@Column(name = "bus_num")
	private String busNumber;
	
	@Column(name = "bus_stop")
	private String busStop;
	
	@Column(name="printStatus")
	private String printStatus;

	@Column(name = "remark", insertable = false)
	private String remark;
	
	@Column(name = "secret_key")
	@JsonProperty(access = Access.READ_ONLY)
	private String secretKey;
	
	@Column(name = "class_attended_2016")
	private String classAttended2016;

	@Column(name = "class_attended_2017")
	private String classAttended2017;
	
	@Column(name = "class_attended_2018")
	private String classAttended2018;
	
	@Column(name = "class_attended_2019")
	private String classAttended2019;
	
	@Column(name = "class_room_no_2016")
	private String classRoomNo2016;

	@Column(name = "class_room_no_2017")
	private String classRoomNo2017;

	@Column(name = "class_room_no_2018")
	private String classRoomNo2018;

	@Column(name = "class_room_no_2019")
	private String classRoomNo2019;

	@Column(name = "attendance_2016")
	private String attendance2016;

	@Column(name = "attendance_2017")
	private String attendance2017;
	
	@Column(name = "attendance_2018")
	private String attendance2018;

	@Column(name = "attendance_2019")
	private String attendance2019;
	
	@Column(name = "marks_2016")
	private String marks2016;

	@Column(name = "marks_2017")
	private String marks2017;
	
	@Column(name = "marks_2018")
	private String marks2018;
	
	@Column(name = "marks_2019")
	private String marks2019;
	
	@Column(name = "opt_in_2018")
	private Integer optIn2018;
	
	@Column(name = "opt_in_2019")
	private String optIn2019;

	@Column(name="day1")
	private String day1;
	
	@Column(name="day2")
	private String day2;
	
	@Column(name="day3")
	private String day3;
	
	@Column(name="day4")
	private String day4;
	
	@Column(name="day5")
	private String day5;
	
	@Column(name="day6")
	private String day6;
	
	@Column(name="day7")
	private String day7;

	@Column(name="day8")
	private String day8;

	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	@JsonIgnore
	private Date dateCreatedInDB;
	
	@Column(name = "last_modified_date")
	@Temporal(TemporalType.DATE)
	@JsonIgnore
	private Date dateLastModifiedInDB;
		
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String createdDate;
	
	@Transient
	@JsonProperty(access = Access.READ_ONLY)
	private String lastModifiedDate;

}