package com.bookstore.resource;

import com.bookstore.model.Customer;
import com.bookstore.storage.InMemoryDatabase;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @POST
    public Response addCustomer(Customer customer) {
        int id = InMemoryDatabase.customerIdCounter++;
        customer.setId(id);
        InMemoryDatabase.customers.put(id, customer);

        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public Response getAllCustomers() {
        Collection<Customer> customers = InMemoryDatabase.customers.values();
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") int id) {
        Customer customer = InMemoryDatabase.customers.get(id);
        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found", "message", "Customer with ID " + id + " does not exist."))
                    .build();
        }
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        Customer existing = InMemoryDatabase.customers.get(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found", "message", "Customer with ID " + id + " does not exist."))
                    .build();
        }

        updatedCustomer.setId(id);
        InMemoryDatabase.customers.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        Customer removed = InMemoryDatabase.customers.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Customer Not Found", "message", "Customer with ID " + id + " does not exist."))
                    .build();
        }
        return Response.noContent().build();
    }
}
