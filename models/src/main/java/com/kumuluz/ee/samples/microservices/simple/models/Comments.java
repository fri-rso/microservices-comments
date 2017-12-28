package com.kumuluz.ee.samples.microservices.simple.models;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comments")
@NamedQuery(name = "Comments.findAll", query = "SELECT c FROM Comments c")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String author;

    private String comment;

    private Date date;

    private String serviceReference;

    /*Getters - Setters*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getServiceReference() {
        return serviceReference;
    }

    public void setServiceReference(String serviceReference) {
        this.serviceReference = serviceReference;
    }
}