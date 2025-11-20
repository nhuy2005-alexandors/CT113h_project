package org.example.model;

// Lớp này không cần @Document vì nó sẽ được nhúng
public class Author {
    private String authorId;
    private String name;
    private Integer birthYear;

    // Constructors
    public Author() {}
    public Author(String authorId, String name, Integer birthYear) {
        this.authorId = authorId;
        this.name = name;
        this.birthYear = birthYear;
    }

    // Getters và Setters
    public String getAuthorId() { return authorId; }
    public void setAuthorId(String authorId) { this.authorId = authorId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
}