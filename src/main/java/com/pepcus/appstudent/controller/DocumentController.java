package com.pepcus.appstudent.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.entity.Document;
import com.pepcus.appstudent.service.DocumentService;

/**
 * Controller class for document related services
 * 
 * @author Sandeep Vishwakarma
 * @since 05-02-2020
 *
 */
@RestController
@RequestMapping("/v1/documents")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	/**
	 * Method to create/update document in db and upload/modify document on S3 server
	 * @param file
	 * @param displayName
	 * @param rank
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@PutMapping
	public HttpStatus uploadDocument(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "displayName", required = true) String displayName,
			@RequestParam(value = "rank", required = false) Integer rank,
			@RequestParam(value = "id", required = false) Integer id) throws Exception {
		documentService.uploadDocument(id, displayName, rank, file);
		return HttpStatus.ACCEPTED;
	}

	/**
	 * Method to delete document reference from database and remove document from S3 server
	 * @param documentId
	 * @return
	 * @throws IOException
	 */
	@DeleteMapping(value = "/{id}")
	public HttpStatus deleteDocument(@PathVariable("id") Integer documentId) throws IOException {
		return (documentService.deleteDocument(documentId)) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST;
	}

	/**
	 * Method to return list of all document
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Document>> getDocuments() {
		List<Document> documents = documentService.getDocumentsList();
		return new ResponseEntity<List<Document>>(documents, HttpStatus.OK);
	}

	/**
	 * Method to download document file for given documentId
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/{id}/file")
	public @ResponseBody ResponseEntity<InputStreamResource> downloadDocument(@PathVariable("id") Integer documentId)
			throws Exception {
		return documentService.downloadDocument(documentId);
	}

}
