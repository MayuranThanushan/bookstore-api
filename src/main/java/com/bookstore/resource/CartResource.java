package com.bookstore.resource;

import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryDatabase;

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
        if (!InMemoryDatabase.customers.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found")).build();
        }

        Book book = InMemoryDatabase.books.get(item.getBookId());
        if (book == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Book Not Found")).build();
        }

        if (item.getQuantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid Quantity")).build();
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
        if (!InMemoryDatabase.customers.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found")).build();
        }

        List<CartItem> cart = InMemoryDatabase.carts.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(cart).build();
    }

    @PUT
    @Path("/items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId,
                                   CartItem update) {
        List<CartItem> cart = InMemoryDatabase.carts.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Cart Not Found")).build();
        }

        Optional<CartItem> item = cart.stream().filter(ci -> ci.getBookId() == bookId).findFirst();
        if (item.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Item Not Found")).build();
        }

        if (update.getQuantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Invalid Quantity")).build();
        }

        item.get().setQuantity(update.getQuantity());
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(@PathParam("customerId") int customerId,
                                   @PathParam("bookId") int bookId) {
        List<CartItem> cart = InMemoryDatabase.carts.get(customerId);
        if (cart == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Cart Not Found")).build();
        }

        boolean removed = cart.removeIf(ci -> ci.getBookId() == bookId);
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Item Not Found")).build();
        }

        return Response.noContent().build();
    }
}
