package ibf2022.batch2.csf.backend.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.core.model.Model;
import ibf2022.batch2.csf.backend.models.ArchiveDocument;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;
import ibf2022.batch2.csf.backend.repositories.ImageRepository;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class UploadController {
	@Autowired
	ImageRepository imageRepository;
	
	@Autowired
    ArchiveRepository archiveRepository;

	// TODO: Task 2, Task 3, Task 4
    @PostMapping(path="/upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArchiveDocument> upload(@RequestPart MultipartFile file, @RequestPart String name, @RequestPart String title, @RequestPart String comment, HttpServletRequest request) {
        List<String> fileList = new ArrayList<>();
		String bundledId = "";
		ArchiveDocument archiveDocument = null;
		try{
			bundledId = generateBundleId();
			ZipInputStream zipIn = new ZipInputStream(file.getInputStream());
            ZipEntry entry = zipIn.getNextEntry();
			while (entry != null) {
				String filePath = request.getContextPath() + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					extractFile(zipIn, filePath);	
					File newFile = new File(filePath);
					String fileName = imageRepository.upload(newFile, name, title, comment, bundledId);
					String imageUrl = imageRepository.getImageUrlsFromS3(fileName);
					fileList.add(imageUrl);
				} else {
					File dir = new File(filePath);
					dir.mkdirs();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
			zipIn.close();
			
			archiveRepository.recordBundle(title, name, comment, fileList, bundledId);
			archiveDocument = new ArchiveDocument(bundledId, null, title, name, comment, fileList);
        }catch(Exception e){
			e.printStackTrace();
        }            
        return ResponseEntity.ok(archiveDocument);
    }

	@GetMapping("bundle")
    public List<ArchiveDocument> getBundleByBundleId(Model model, @RequestParam String bundleId) {
        List<ArchiveDocument> bundleList = null;
        try {
			bundleList = archiveRepository.getBundleByBundleId(bundleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundleList;
    }

	@GetMapping("bundles")
    public List<ArchiveDocument> getBundles(Model model) {
        List<ArchiveDocument> bundleList = null;
        try {
			bundleList = archiveRepository.getBundles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundleList;
    }

	private String generateBundleId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

	private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
