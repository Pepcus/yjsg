package com.pepcus.appstudent.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "duplicate_registration")
@Data
public class DuplicateRegistration {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "duplicate_of_student_id")
    private Integer duplicateOfStudentId;
    
    @Column(name = "duplicate_student_json",columnDefinition = "text")
    private String duplicateStudentJson;

}
