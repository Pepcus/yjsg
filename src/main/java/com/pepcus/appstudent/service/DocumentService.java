package com.pepcus.appstudent.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pepcus.appstudent.convertor.DocumentEntityConvertor;
import com.pepcus.appstudent.entity.Document;
import com.pepcus.appstudent.exception.BadRequestException;
import com.pepcus.appstudent.repository.DocumentRepository;
import com.pepcus.appstudent.util.FileStream;
import com.pepcus.appstudent.util.FileUtil;
import com.pepcus.appstudent.util.MediaHandler;
import com.pepcus.appstudent.util.MediaHandlerHelper;
import com.pepcus.appstudent.util.S3FileUtils;

/**
 * Service class to manage document related business operations
 * 
 * @author Sandeep Vishwakarma
 * @since 05-02-2020
 *
 */
@Service
public class DocumentService {

	@Value("${document_temp_upload_path}")
	String documentTempUploadPath;

	@Value("${document_bucket}")
	String documentsBucket;

	@Value("${document_folder}")
	String documentFolder;

	@Value("${document_path}")
	String documentsPath;

	@Autowired
	DocumentRepository documentRepository;

	@Autowired
	MediaHandler mediaHandler;

	@Autowired
	S3FileUtils s3FileUtils;

	/**
	 * Method to upload document
	 * @param id
	 * @param displayName
	 * @param rank
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public Document uploadDocument(Integer id, String displayName, Integer rank, MultipartFile file) throws Exception {
		Document documentEntity = (id != null) ? getDocumentEntity(id) : null;
		documentEntity = (documentEntity == null) ? DocumentEntityConvertor.convertToDocumentEntity(displayName, rank)
				: DocumentEntityConvertor.convertToDocumentEntity(documentEntity, displayName, rank);
		uploadDocuments(documentEntity, file);
		persistDocumentEntity(documentEntity);
		return documentEntity;
	}

	/**
	 * Method to persist document entity
	 * @param documentEntity
	 */
	@Transactional
	public void persistDocumentEntity(Document documentEntity) {
		documentRepository.save(documentEntity);
	}

	/**
	 * Method to upload document on S3 server
	 * @param documentEntity
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 */
	private Document uploadDocuments(Document documentEntity, MultipartFile multipartFile) throws IOException {
		// Get file from media server
		String fileName = multipartFile.getOriginalFilename();
		InputStream inputStream = multipartFile.getInputStream();

		// Upload file at temp location to upload on S3
		FileUtil.uploadFile(inputStream, documentTempUploadPath, fileName);

		// Upload file on S3
		String mediaKey = documentFolder;
		String fileUrl = s3FileUtils.uploadDocument(documentsBucket, mediaKey, documentTempUploadPath, fileName, mediaHandler);
		documentEntity.setUrl(fileUrl);
		FileStream fileStream = MediaHandlerHelper.loadFile(mediaHandler, documentsBucket, mediaKey, fileName);
		documentEntity.setDocumentUploadedDate(fileStream.getLastModified());
		s3FileUtils.cleanup(documentTempUploadPath, fileName);

		return documentEntity;
	}

	/**
	 * Method to fetch all document from database
	 * @return
	 */
	public List<Document> getDocumentsList() {
		return DocumentEntityConvertor.setDateInDocumentEntityList(documentRepository.findAll());
	}

	/**
	 * Method to get document for given documentId
	 * @param documentId
	 * @return
	 */
	public Document getDocumentEntity(Integer documentId) {
		Document documentEntity = documentRepository.findOne(documentId);
		if (null == documentEntity) {
			throw new BadRequestException("No Document found for id : " + documentId);
		}
		return documentEntity;

	}

	/**
	 * Method to delete document reference from DB and also remove document file from S3 server
	 * @param documentId
	 * @return
	 */
	public boolean deleteDocument(Integer documentId) {
		Document documentEntity = getDocumentEntity(documentId);
		String fileName = FileUtil.getFileNameFromUrl(documentEntity.getUrl());
		String mediaKey = documentFolder;
		Boolean isDocumentDelete = s3FileUtils.deleteDocument(documentsBucket, mediaKey, fileName, mediaHandler);
		if (isDocumentDelete) {
			documentRepository.delete(documentEntity);
		}
		return isDocumentDelete;
	}

	/**
	 * Method to fetch document file from S3 server and make available for download for given documentId
	 * @param documentId
	 * @return
	 * @throws Exception
	 */
	public ResponseEntity<InputStreamResource> downloadDocument(Integer documentId) throws Exception {
		Document documentEntity = getDocumentEntity(documentId);

		// Get filename from fileUrl
		String fileUrl = documentEntity.getUrl();
		fileUrl = URLDecoder.decode(fileUrl, "UTF-8");
		String bucketName = documentsBucket;

		String mediaKey = documentFolder;
		String mediaName = FileUtil.getFileNameFromUrl(fileUrl);
		return s3FileUtils.downloadDocumentFromServer(bucketName, mediaKey, mediaName, mediaHandler);
	}

}
