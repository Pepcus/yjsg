package com.pepcus.appstudent.convertor;

import static com.pepcus.appstudent.util.CommonUtil.convertDateToString;

import java.util.Date;
import java.util.List;

import com.pepcus.appstudent.entity.Document;

/**
 * Converter class to build/convert document entity
 * 
 * @author Sandeep Vishwakarma
 * @since 05-02-2020
 *
 */
public class DocumentEntityConvertor {

	public static Document convertToDocumentEntity(String displayName, Integer rank) {
		Document document = new Document();
		document.setDateCreatedInDB(new Date());
		convertToDocumentEntity(document, displayName, rank);
		return document;
	}

	public static Document convertToDocumentEntity(Document document, String displayName, Integer rank) {
		if (displayName != null) {
			document.setDisplayName(displayName);
		}
		if (rank != null) {
			document.setRank(rank);
		}
		document.setDateLastModifiedInDB(new Date());
		return document;
	}

	public static List<Document> setDateInDocumentEntityList(List<Document> documents) {
		if (documents != null && !documents.isEmpty()) {
			for (Document document : documents) {
				setDateInDocumentEntity(document);
			}
		}
		return documents;
	}

	public static Document setDateInDocumentEntity(Document document) {
		if (null != document.getDateLastModifiedInDB()) {
			document.setLastModifiedDate(convertDateToString(document.getDateLastModifiedInDB()));
		}
		if (null != document.getDateCreatedInDB()) {
			document.setCreatedDate(convertDateToString(document.getDateCreatedInDB()));
		}
		if (null != document.getDocumentUploadedDate()) {
			document.setUploadedDate(convertDateToString(document.getDocumentUploadedDate()));
		}
		return document;
	}

}
