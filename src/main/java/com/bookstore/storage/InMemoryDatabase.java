package com.bookstore.storage;

import com.bookstore.model.*;

import java.util.*;

public class InMemoryDatabase {
    public static final Map<Integer, Book> books = new HashMap<>();
    public static final Map<Integer, Author> authors = new HashMap<>();
    public static final Map<Integer, Customer> customers = new HashMap<>();
    public static final Map<Integer, List<CartItem>> carts = new HashMap<>();
    public static final Map<Integer, List<Order>> orders = new HashMap<>();

    public static int bookIdCounter = 1;
    public static int authorIdCounter = 1;
    public static int customerIdCounter = 1;
    public static int orderIdCounter = 1;
}
