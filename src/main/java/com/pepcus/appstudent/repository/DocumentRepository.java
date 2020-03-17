package com.pepcus.appstudent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pepcus.appstudent.entity.Document;

/**
 * Repository for document entity
 * 
 * @author Sandeep Vishwakarma
 * @since 05-02-2020
 *
 */
public interface DocumentRepository extends JpaRepository<Document, Integer>, JpaSpecificationExecutor<Document> {

}
