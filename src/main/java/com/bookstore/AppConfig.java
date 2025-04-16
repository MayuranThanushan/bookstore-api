package com.bookstore;

import org.glassfish.jersey.server.ResourceConfig;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("com.bookstore.resource", "com.bookstore.exception.mappers");
    }
}
