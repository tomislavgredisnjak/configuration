package net.veniture.exercise.jira.rest;


import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.querydsl.core.annotations.QueryEntity;


import com.sun.jndi.toolkit.url.Uri;
import net.veniture.exercise.jira.entity.Book;
import net.veniture.exercise.jira.entity.BookService;
import net.veniture.exercise.jira.settings.AppSettings;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.xpath.XPath;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
@Scanned
@Path("/book")
public class BookListResource extends JiraWebActionSupport {

    private static final Logger log = LoggerFactory.getLogger(BookListResource.class);

    private String author;

    private String title;

    private BookService bookService;

    @Autowired
    public BookListResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@Consumes({MediaType.APPLICATION_JSON})
    public Response getAvailableBooks(@QueryParam("max") Integer maxItemsToFetch, @QueryParam("offset") Integer offset){

        List<BookDTO> foundBooks = new ArrayList<>();

        for (Book foundBook : bookService.getAllBooks()) {
            BookDTO bookDTO = new BookDTO(foundBook);
            foundBooks.add(bookDTO);
        }

        return Response.ok(new Object(){
            public List<BookDTO> books = foundBooks;
            public String test = "Lorem ipsum dolor sit amet";
        }).build();
    }

    @DELETE
    @Path("/{ID}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deleteBook(@PathParam("ID") Integer ID){
        bookService.deleteBookById(ID);

        return Response.noContent().build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response addBook(@QueryParam("title") String title, @QueryParam("author") String author) throws URISyntaxException {
        bookService.addBook(title, author);
        String myUri = "http://localhost:2990/jira/rest/exercise/1.0/book/";
        URI uri = new URI(myUri);
        return Response.created(uri).build();
    }

    @Override
    public String doExecute() throws Exception {
        log.error("doExecute");

        addBook(title, author);

        this.setReturnUrl("http://localhost:2990/jira/browse/TEST-1");
        return this.getRedirect(this.getReturnUrl());
    }
}
