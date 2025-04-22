package com.bookstore.resource;

import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;
import com.bookstore.utils.EntityUtils;

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
        Author author = EntityUtils.findAuthorOrThrow(id);
        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") int id, Author updated) {
        EntityUtils.findAuthorOrThrow(id);
        updated.setId(id);
        InMemoryDatabase.authors.put(id, updated);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        EntityUtils.findAuthorOrThrow(id);
        InMemoryDatabase.authors.remove(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") int authorId) {
        EntityUtils.findAuthorOrThrow(authorId);
        List<Book> books = InMemoryDatabase.books.values().stream()
                .filter(book -> book.getAuthorId() == authorId)
                .toList();
        return Response.ok(books).build();
    }
}
