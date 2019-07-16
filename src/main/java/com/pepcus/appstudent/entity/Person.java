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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * Entity class used to map table in DB
 */
@Entity
@Table(name = "person")
@Data
@JsonInclude(Include.ALWAYS)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "members")
    private Integer members;

    @Column(name = "phoneNumber")
    private String phoneNumber;

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
