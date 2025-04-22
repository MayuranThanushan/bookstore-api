package com.bookstore.utils;

import com.bookstore.exception.*;
import com.bookstore.model.*;
import com.bookstore.storage.InMemoryDatabase;

import java.util.List;

public class EntityUtils {

    public static Book findBookOrThrow(int bookId) {
        Book book = InMemoryDatabase.books.get(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        return book;
    }

    public static Author findAuthorOrThrow(int authorId) {
        Author author = InMemoryDatabase.authors.get(authorId);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + authorId + " not found.");
        }
        return author;
    }

    public static Customer findCustomerOrThrow(int customerId) {
        Customer customer = InMemoryDatabase.customers.get(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        return customer;
    }

    public static List<CartItem> findCartOrThrow(int customerId) {
        List<CartItem> cart = InMemoryDatabase.carts.get(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer " + customerId + " not found.");
        }
        return cart;
    }
}
