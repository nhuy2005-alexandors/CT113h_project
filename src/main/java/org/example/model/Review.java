package org.example.model;

// Lớp này không cần @Document vì nó sẽ được nhúng
public class Review {
    private String reviewerName;
    private Integer rating; // 1 đến 5 sao
    private String comment;

    // Constructors
    public Review() {}
    public Review(String reviewerName, Integer rating, String comment) {
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters và Setters
    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}