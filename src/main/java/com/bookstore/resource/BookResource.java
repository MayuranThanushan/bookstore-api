package com.bookstore.resource;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;
import com.bookstore.utils.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @POST
    public Response addBook(Book book) {
        int id = InMemoryDatabase.bookIdCounter++;
        book.setId(id);
        InMemoryDatabase.books.put(id, book);
        return Response.status(Response.Status.CREATED)
                .entity(book)
                .build();
    }

    @GET
    public Response getAllBooks() {
        Collection<Book> books = InMemoryDatabase.books.values();
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = EntityUtils.findBookOrThrow(id);
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        EntityUtils.findBookOrThrow(id); // just validate existence
        updatedBook.setId(id);
        InMemoryDatabase.books.put(id, updatedBook);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        EntityUtils.findBookOrThrow(id);
        InMemoryDatabase.books.remove(id);
        return Response.noContent().build();
    }
}
