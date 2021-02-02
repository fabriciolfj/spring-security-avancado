package com.github.fabriciolfj.securitymethod;

import java.util.List;

public class Employee {

    private String name;
    private List<String> books;
    private List<String> roles;

    public Employee(String name, List<String> books, List<String> roles) {
        this.name = name;
        this.books = books;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
