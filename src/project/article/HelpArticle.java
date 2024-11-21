package project.article;

import java.time.Instant;
import java.security.*;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;


/**
 * <p> HelpArticle class </p>
 * 
 * <p> Description: build the help articles for the user, based on the user data/inputs </p>
 * 
 * @version 1.00 2024-10-30 Initial baseline
 */

public class HelpArticle {
    private long id; // Unique identifier
    private String level; // e.g., beginner, intermediate, advanced, expert
    private String groupIdentifier; // Grouping identifier for related articles
    private String access; // Who has access to the article
    private String title; // Title of the article
    private String shortDescription; // Short description or abstract
    private Object[] keywords; // Keywords for searching
    private String body; // The body of the article
    private Object[] referenceLinks; // Links to reference materials
    private String sensitiveTitle; // Title without sensitive information
    private String sensitiveDescription; // Description without sensitive information
    private Instant createdDate; // Date of creation
    private Instant updatedDate; // Date of last update

    // Getters and setters can be added here
    public HelpArticle(long id, String level, String groupIdentifier, String access, String title, String shortDescription, Object[] keywords, String body, Object[] referenceLinks, String sensitiveTitle, String sensitiveDescription) {
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
    
    // Constructor that includes createdDate and updatedDate
    public HelpArticle(long id, String level, String groupIdentifier, String access, String title, String shortDescription, Object[] keywords, String body, Object[] referenceLinks, String sensitiveTitle, String sensitiveDescription, String createdDate, String updatedDate) {
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
    	this.createdDate = Instant.parse(createdDate);
    	this.updatedDate = Instant.parse(updatedDate);
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
    
    public String[] getGroupIdentifierArray() {
        // Assuming groupIdentifier is a String that may contain "a, b, c"
        String groupIdentifier = this.groupIdentifier; // Adjust based on how you store it
        return Arrays.stream(groupIdentifier.split(","))
                     .map(String::trim) // Trim whitespace
                     .toArray(String[]::new); // Convert to String array
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

    public Object[] getKeywords() {
        return keywords;
    }

    public String getBody() {
        return body;
    }

    public Object[] getReferenceLinks() {
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
    
 // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setGroupIdentifier(String groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setReferenceLinks(String[] referenceLinks) {
        this.referenceLinks = referenceLinks;
    }
    
    public void setSensitiveTitle(String sensitiveTitle) {
        this.sensitiveTitle = sensitiveTitle;
    }

    public void setSensitiveDescription(String sensitiveDescription) {
        this.sensitiveDescription = sensitiveDescription;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }
    
}
