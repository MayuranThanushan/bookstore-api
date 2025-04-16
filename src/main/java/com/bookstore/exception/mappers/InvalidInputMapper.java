package com.bookstore.exception.mappers;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidInputMapper implements ExceptionMapper<InvalidInputException> {
    @Override
    public Response toResponse(InvalidInputException ex) {
        ErrorResponse error = new ErrorResponse("Author Not Found", ex.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
