package ibf2022.batch2.csf.backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.ArchiveDocument;

@Repository
public class ArchiveRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	//TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public ArchiveDocument recordBundle(String title, String name, String comments, List<String> imageUrls, String bundledId) {
		LocalDateTime uploadDate = LocalDateTime.now();
        ArchiveDocument archiveDocument = new ArchiveDocument(bundledId, uploadDate, title, name, comments, imageUrls);
        mongoTemplate.insert(archiveDocument, "archives");
		return archiveDocument;
	}

	//TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public List<ArchiveDocument> getBundleByBundleId(String bundleId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("bundledId").is(bundleId));
		List<ArchiveDocument> budleList = mongoTemplate.find(query, ArchiveDocument.class);
		return budleList;
	}

	//TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	//
	//
	public List<ArchiveDocument> getBundles() {
		Query query = new Query();
		query.with(Sort.by(Sort.Direction.ASC, "_id"));
		List<ArchiveDocument> budleList = mongoTemplate.find(query, ArchiveDocument.class);
		return budleList;
	}
}
