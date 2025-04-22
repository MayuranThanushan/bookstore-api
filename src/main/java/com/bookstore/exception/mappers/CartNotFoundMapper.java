package com.bookstore.exception.mappers;

import com.bookstore.exception.CartNotFoundException;
import com.bookstore.exception.ErrorResponse;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CartNotFoundMapper implements ExceptionMapper<CartNotFoundException> {
    @Override
    public Response toResponse(CartNotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse("Cart Not Found", ex.getMessage()))
                .build();
    }
}
