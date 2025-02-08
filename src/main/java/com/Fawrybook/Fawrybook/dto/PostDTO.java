package com.Fawrybook.Fawrybook.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private Long id;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private int likes;
    private List<CommentDTO> comments;
    private double averageLikes = 0.0;
    private  String twitterShareUrl;

    public PostDTO(Long id, String title, String content, LocalDateTime createdAt, int likes, List<CommentDTO> comments,double averageLikes,String twitterShareUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.likes = likes;
        this.comments = comments;
        this.averageLikes = averageLikes;
        this.twitterShareUrl = twitterShareUrl;

    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public double getAverageLikes() {
        return averageLikes;
    }

    public void setAverageLikes(double averageLikes) {
        this.averageLikes = averageLikes;
    }

    public String getTwitterShareUrl() {
        return twitterShareUrl;
    }

    public void setTwitterShareUrl(String twitterShareUrl) {
        this.twitterShareUrl = twitterShareUrl;
    }
}
