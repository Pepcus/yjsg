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

	protected static final String TEMP_UPLOAD_URL = DocumentService.class.getClassLoader().getResource("temp_upload")
			.getPath();

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

	public Document uploadDocument(Integer id, String displayName, Integer rank, MultipartFile file) throws Exception {
		Document documentEntity = (id != null) ? getDocumentEntity(id) : null;
		documentEntity = (documentEntity == null) ? DocumentEntityConvertor.convertToDocumentEntity(displayName, rank)
				: DocumentEntityConvertor.convertToDocumentEntity(documentEntity, displayName, rank);
		uploadDocuments(documentEntity, file);
		persistDocumentEntity(documentEntity);
		return documentEntity;
	}

	@Transactional
	public void persistDocumentEntity(Document documentEntity) {
		documentRepository.save(documentEntity);
	}

	private Document uploadDocuments(Document documentEntity, MultipartFile multipartFile) throws IOException {
		// Get file from media server
		String fileName = multipartFile.getOriginalFilename();
		InputStream inputStream = multipartFile.getInputStream();

		// Upload file at temp location to upload on S3
		FileUtil.uploadFile(inputStream, TEMP_UPLOAD_URL, fileName);

		// Upload file on S3
		String mediaKey = documentFolder;
		String fileUrl = s3FileUtils.uploadDocument(documentsBucket, mediaKey, TEMP_UPLOAD_URL, fileName, mediaHandler);
		documentEntity.setUrl(fileUrl);
		FileStream fileStream = MediaHandlerHelper.loadFile(mediaHandler, documentsBucket, mediaKey, fileName);
		documentEntity.setDocumentUploadedDate(fileStream.getLastModified());
		s3FileUtils.cleanup(TEMP_UPLOAD_URL, fileName);

		return documentEntity;
	}

	public List<Document> getDocumentsList() {
		return DocumentEntityConvertor.setDateInDocumentEntityList(documentRepository.findAll());
	}

	public Document getDocumentEntity(Integer documentId) {
		Document documentEntity = documentRepository.findOne(documentId);
		if (null == documentEntity) {
			throw new BadRequestException("No Document found for id : " + documentId);
		}
		return documentEntity;

	}

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