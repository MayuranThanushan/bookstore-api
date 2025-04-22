package com.bookstore.exception.mappers;

import com.bookstore.exception.OutOfStockException;
import com.bookstore.exception.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OutOfStockMapper implements ExceptionMapper<OutOfStockException> {
    @Override
    public Response toResponse(OutOfStockException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Out of Stock", ex.getMessage()))
                .build();
    }
}
