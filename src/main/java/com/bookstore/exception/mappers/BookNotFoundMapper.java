package com.bookstore.exception.mappers;

import com.bookstore.exception.BookNotFoundException;
import com.bookstore.exception.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BookNotFoundMapper implements ExceptionMapper<BookNotFoundException> {
    @Override
    public Response toResponse(BookNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Book Not Found", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
