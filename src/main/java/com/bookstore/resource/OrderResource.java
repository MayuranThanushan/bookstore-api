package com.bookstore.resource;

import com.bookstore.exception.InvalidInputException;
import com.bookstore.exception.OutOfStockException;
import com.bookstore.model.Book;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.storage.InMemoryDatabase;
import com.bookstore.utils.EntityUtils;

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
        EntityUtils.findCustomerOrThrow(customerId);
        List<CartItem> cart = EntityUtils.findCartOrThrow(customerId);

        if (cart.isEmpty()) {
            throw new InvalidInputException("Cart is empty.");
        }

        double total = 0.0;
        for (CartItem item : cart) {
            Book book = EntityUtils.findBookOrThrow(item.getBookId());
            if (book.getStock() < item.getQuantity()) {
                throw new OutOfStockException("Book with ID " + book.getId() + " is out of stock.");
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
        InMemoryDatabase.carts.remove(customerId); // Clear cart after ordering

        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getAllOrders(@PathParam("customerId") int customerId) {
        EntityUtils.findCustomerOrThrow(customerId);
        List<Order> orders = InMemoryDatabase.orders.getOrDefault(customerId, new ArrayList<>());
        return Response.ok(orders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(@PathParam("customerId") int customerId,
                                 @PathParam("orderId") int orderId) {
        EntityUtils.findCustomerOrThrow(customerId);

        List<Order> orders = InMemoryDatabase.orders.get(customerId);
        if (orders == null) {
            throw new InvalidInputException("No orders found for this customer.");
        }

        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return Response.ok(order).build();
            }
        }

        throw new InvalidInputException("Order with ID " + orderId + " not found.");
    }
}