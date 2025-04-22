package com.bookstore.resource;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;
import com.bookstore.utils.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") int customerId, CartItem item) {
        EntityUtils.findCustomerOrThrow(customerId);
        Book book = EntityUtils.findBookOrThrow(item.getBookId());

        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }

        List<CartItem> cart = InMemoryDatabase.carts.computeIfAbsent(customerId, k -> new ArrayList<>());

        Optional<CartItem> existing = cart.stream()
                .filter(ci -> ci.getBookId() == item.getBookId())
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + item.getQuantity());
        } else {
            cart.add(item);
        }

        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Response viewCart(@PathParam("customerId") int customerId) {
        EntityUtils.findCustomerOrThrow(customerId);
        List<CartItem> cart = InMemoryDatabase.carts.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId,
                                   CartItem update) {
        EntityUtils.findCustomerOrThrow(customerId);
        Book book = EntityUtils.findBookOrThrow(bookId);

        List<CartItem> cart = EntityUtils.findCartOrThrow(customerId);

        Optional<CartItem> item = cart.stream().filter(ci -> ci.getBookId() == bookId).findFirst();
        if (item.isEmpty()) {
            throw new InvalidInputException("Item not found in cart.");
        }

        if (update.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero.");
        }

        item.get().setQuantity(update.getQuantity());
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId) {
        EntityUtils.findCustomerOrThrow(customerId);
        Book book = EntityUtils.findBookOrThrow(bookId);

        List<CartItem> cart = EntityUtils.findCartOrThrow(customerId);
        boolean removed = cart.removeIf(ci -> ci.getBookId() == bookId);

        if (!removed) {
            throw new InvalidInputException("Item not found in cart.");
        }

        return Response.noContent().build();
    }
}