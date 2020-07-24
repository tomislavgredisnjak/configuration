package net.veniture.exercise.jira.entity;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import net.java.ao.Query;

import javax.inject.Named;
import java.sql.SQLException;

@Scanned
@Named
public class BookServiceImpl implements BookService {

    @ComponentImport
    private ActiveObjects ao;

    public BookServiceImpl(ActiveObjects ao){
        this.ao = ao;
    }

    @Override
    public Book getBookById(Integer id) {
        return ao.get(Book.class, id);
    }

    @Override
    public Book[] getAllBooks() {
        return ao.find(Book.class);
    }

    @Override
    public Book[] findBooks(String searchParam) {
        return ao.find(Book.class, Query.select().where("AUTHOR LIKE ? OR TITLE LIKE ?", searchParam, searchParam));
    }

    @Override
    public Book[] getBooks(Integer offset, Integer max) {
        return ao.find(Book.class, Query.select().offset(offset).limit(max));
    }

    @Override
    public Book addBook(String title, String author) {
        Book newBook = ao.create(Book.class);
        newBook.setTitle(title);
        newBook.setAuthor(author);
        newBook.save();
        return newBook;
    }

    @Override
    public void deleteBookById(Integer ID) {
        Book book = getBookById(ID);
        ao.delete(book);
    }


}
