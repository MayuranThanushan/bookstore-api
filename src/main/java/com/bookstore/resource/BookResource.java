package com.bookstore.resource;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;

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
        Book book = InMemoryDatabase.books.get(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book updatedBook) {
        Book existing = InMemoryDatabase.books.get(id);
        if (existing == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        updatedBook.setId(id);
        InMemoryDatabase.books.put(id, updatedBook);
        return Response.ok(updatedBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book removed = InMemoryDatabase.books.remove(id);
        if (removed == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return Response.noContent().build();
    }
}
