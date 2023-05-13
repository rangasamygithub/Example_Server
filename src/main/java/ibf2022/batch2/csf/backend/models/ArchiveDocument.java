package ibf2022.batch2.csf.backend.models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("archives")
public class ArchiveDocument {
    @Id
    private String bundledId;
    private LocalDateTime date;
    private String title;
    private String name;
    private String comments;
    private List<String> urls;

    public ArchiveDocument(String bundledId, LocalDateTime date, String title, String name, String comments, List<String> urls) {
        this.bundledId = bundledId;
        this.date = date;
        this.title = title;
        this.name = name;
        this.comments = comments;
        this.urls = urls;
    }

    public String getBundledId() {
        return bundledId;
    }
    public void setBundledId(String bundledId) {
        this.bundledId = bundledId;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}
