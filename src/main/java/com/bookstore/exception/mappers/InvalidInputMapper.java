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
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Invalid Input", ex.getMessage()))
                .build();
    }
}
