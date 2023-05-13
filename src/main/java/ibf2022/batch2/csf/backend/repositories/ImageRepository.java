package ibf2022.batch2.csf.backend.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Repository
public class ImageRepository {

	@Autowired
    private AmazonS3 amazonS3;
	
	@Value("${do.storage.bucketname}")
    private String s3BucketName;
	
	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public String upload(File file, String title, String name, String comments, String bundledId) {
		Map<String, String> userData = new HashMap<>();
		String key = "";
		String filenameExt = "";
		try {
			userData.put("bundledId", bundledId);
			userData.put("date", LocalDateTime.now().toString());
			userData.put("filename", file.getName());
			key = UUID.randomUUID().toString().substring(0, 8);
			StringTokenizer tk = new StringTokenizer(file.getName(), ".");
			int count = 0;
			
			while(tk.hasMoreTokens()){
				if(count == 1){
					filenameExt = tk.nextToken();
					break;
				}else{
					filenameExt = tk.nextToken();
					count++;
				}
			}
			PutObjectRequest request = new PutObjectRequest(s3BucketName, key+"."+filenameExt, file);
            ObjectMetadata metadata = new ObjectMetadata();
			Path path = file.toPath();
    		String mimeType = Files.probeContentType(path);
			long bytes = Files.size(path);
			metadata.setContentLength(bytes / 1024);
            metadata.setContentType(mimeType);
            metadata.setUserMetadata(userData);
            request.setMetadata(metadata);
            amazonS3.putObject(request.withCannedAcl(CannedAccessControlList.PublicRead));					
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key+"."+filenameExt;
	}

	public String getImageUrlsFromS3(String fileName) {
		return amazonS3.getUrl(s3BucketName, fileName).toString();
	}
}
