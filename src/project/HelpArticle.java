package project;

import java.time.Instant;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class HelpArticle {
    private long id; // Unique identifier
    private String level; // e.g., beginner, intermediate, advanced, expert
    private String groupIdentifier; // Grouping identifier for related articles
    private String access; // Who has access to the article
    private String title; // Title of the article
    private String shortDescription; // Short description or abstract
    private String[] keywords; // Keywords for searching
    private String body; // The body of the article
    private String[] referenceLinks; // Links to reference materials
    private String sensitiveTitle; // Title without sensitive information
    private String sensitiveDescription; // Description without sensitive information
    private Instant createdDate; // Date of creation
    private Instant updatedDate; // Date of last update

    // Getters and setters can be added here
    public HelpArticle(long id, String level, String groupIdentifier, String access, String title, String shortDescription, String[] keywords, String body, String[] referenceLinks, String sensitiveTitle, String sensitiveDescription) {
    	this.id = id;
    	this.level = level;
    	this.groupIdentifier = groupIdentifier;
    	this.access = access;
    	this.title = title;
    	this.shortDescription = shortDescription;
    	this.keywords = keywords;
    	this.body = body;
    	this.referenceLinks = referenceLinks;
    	this.sensitiveTitle = sensitiveTitle;
    	this.sensitiveDescription = sensitiveDescription;
    	this.createdDate = Instant.now();
    	this.updatedDate = Instant.now();
    }

    // Method to generate a unique ID based on the article's content
    public long generateUniqueId() {
        String combined = level + groupIdentifier + access + title; // Combine relevant fields
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            return Math.abs(hash.hashCode()); // Use the hash code as a unique ID
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return -1; // Return an invalid ID in case of error
        }
    }

    public void setId(long id) {
        this.id = id; // Set the unique identifier
    }
    
    // Getter methods
    public long getId() {
    	return id;
    }

    public String getLevel() {
    	return level;
    }

    public String getGroupIdentifier() {
        return groupIdentifier;
    }

    public String getAccess() {
        return access;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public String getBody() {
        return body;
    }

    public String[] getReferenceLinks() {
        return referenceLinks;
    }

    public String getSensitiveTitle() {
        return sensitiveTitle;
    }

    public String getSensitiveDescription() {
        return sensitiveDescription;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

}
