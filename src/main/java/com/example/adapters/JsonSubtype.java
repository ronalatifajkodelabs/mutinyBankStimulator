package com.example.adapters;

public @interface JsonSubtype {
    Class<?> clazz();

    String name();
}
