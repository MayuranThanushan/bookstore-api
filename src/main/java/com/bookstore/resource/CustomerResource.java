package com.bookstore.resource;

import com.bookstore.model.Customer;
import com.bookstore.storage.InMemoryDatabase;
import com.bookstore.utils.EntityUtils;

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
        Customer customer = EntityUtils.findCustomerOrThrow(id);
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") int id, Customer updatedCustomer) {
        EntityUtils.findCustomerOrThrow(id);
        updatedCustomer.setId(id);
        InMemoryDatabase.customers.put(id, updatedCustomer);
        return Response.ok(updatedCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") int id) {
        EntityUtils.findCustomerOrThrow(id);
        InMemoryDatabase.customers.remove(id);
        return Response.noContent().build();
    }
}
