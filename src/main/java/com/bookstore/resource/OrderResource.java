package com.bookstore.resource;

import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.storage.InMemoryDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @POST
    public Response placeOrder(@PathParam("customerId") int customerId) {
        if (!InMemoryDatabase.customers.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found")).build();
        }

        List<CartItem> cart = InMemoryDatabase.carts.get(customerId);
        if (cart == null || cart.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Cart is empty")).build();
        }

        double total = 0.0;
        for (CartItem item : cart) {
            Book book = InMemoryDatabase.books.get(item.getBookId());
            if (book == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("error", "Book Not Found")).build();
            }
            if (book.getStock() < item.getQuantity()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Out of Stock", "bookId", book.getId())).build();
            }
            total += book.getPrice() * item.getQuantity();
        }

        // Deduct stock
        for (CartItem item : cart) {
            Book book = InMemoryDatabase.books.get(item.getBookId());
            book.setStock(book.getStock() - item.getQuantity());
        }

        int orderId = InMemoryDatabase.orderIdCounter++;
        Order order = new Order(orderId, customerId, new ArrayList<>(cart), total);

        InMemoryDatabase.orders.computeIfAbsent(customerId, k -> new ArrayList<>()).add(order);
        InMemoryDatabase.carts.remove(customerId);

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        if (!InMemoryDatabase.customers.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found")).build();
        }

        List<Order> orders = InMemoryDatabase.orders.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId,
                                 @PathParam("orderId") int orderId) {
        if (!InMemoryDatabase.customers.containsKey(customerId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found")).build();
        }

        List<Order> orders = InMemoryDatabase.orders.get(customerId);
        if (orders == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "No Orders Found")).build();
        }

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return Response.ok(order).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", "Order Not Found")).build();
    }
}
