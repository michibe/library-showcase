package org.michibe.libraryshowcase.api;

public class Paths {

    public static final String PREFIX = "/api";


    public static final String CREATE_ACCESS_TOKEN = PREFIX + "/access-token";


    public static final String CREATE_BOOK = PREFIX + "/books";
    public static final String GET_ALL_BOOKS = PREFIX + "/books";
    public static final String GET_BOOK = PREFIX + "/books/{bookId}";
    public static final String UPDATE_BOOK = PREFIX + "/books/{bookId}";
    public static final String DELETE_BOOK = PREFIX + "/books/{bookId}";


    public static final String CREATE_CATEGORY = PREFIX + "/categories";
    public static final String GET_ALL_CATEGORIES = PREFIX + "/categories";
    public static final String GET_CATEGORY = PREFIX + "/categories/{categoryId}";
    public static final String UPDATE_CATEGORY = PREFIX + "/categories/{categoryId}";
    public static final String DELETE_CATEGORY = PREFIX + "/categories/{categoryId}";

    public static final String CREATE_USER = PREFIX + "/users";
    public static final String GET_ALL_USERS = PREFIX + "/users";
    public static final String GET_USER = PREFIX + "/users/{userId}";
    public static final String UPDATE_USER = PREFIX + "/users/{userId}";
    public static final String DELETE_USER = PREFIX + "/users/{userId}";

}
