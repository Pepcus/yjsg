package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.pepcus.appstudent.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer>, JpaSpecificationExecutor<Person> {

}
