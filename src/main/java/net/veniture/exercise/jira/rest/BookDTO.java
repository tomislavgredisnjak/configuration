package net.veniture.exercise.jira.rest;

import net.veniture.exercise.jira.entity.Book;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BookDTO {
    public Integer id;
    public String title;
    public String author;

    public BookDTO(String title, String author, Integer id){
        this.title = title;
        this.author = author;
        this.id = id;
    }

    public BookDTO(Book book){
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.id = book.getID();
    }
}
