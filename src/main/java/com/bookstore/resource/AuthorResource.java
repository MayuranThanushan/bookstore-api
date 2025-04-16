package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    @POST
    public Response addAuthor(Author author) {
        int id = InMemoryDatabase.authorIdCounter++;
        author.setId(id);
        InMemoryDatabase.authors.put(id, author);

        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public Response getAllAuthors() {
        Collection<Author> authors = InMemoryDatabase.authors.values();
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") int id) {
        Author author = InMemoryDatabase.authors.get(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Author Not Found", "message", "Author with ID " + id + " does not exist."))
                    .build();
        }
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updatedAuthor) {
        Author existing = InMemoryDatabase.authors.get(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Author Not Found", "message", "Author with ID " + id + " does not exist."))
                    .build();
        }
        updatedAuthor.setId(id);
        InMemoryDatabase.authors.put(id, updatedAuthor);
        return Response.ok(updatedAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        Author removed = InMemoryDatabase.authors.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Author Not Found", "message", "Author with ID " + id + " does not exist."))
                    .build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int authorId) {
        if (!InMemoryDatabase.authors.containsKey(authorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Author Not Found", "message", "Author with ID " + authorId + " does not exist."))
                    .build();
        }

        List<Book> booksByAuthor = InMemoryDatabase.books.values().stream()
                .filter(book -> book.getAuthorId() == authorId)
                .collect(Collectors.toList());

        return Response.ok(booksByAuthor).build();
    }
}
