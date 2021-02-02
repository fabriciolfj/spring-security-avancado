package com.github.fabriciolfj.securitymethod;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private Map<String, Employee> records =
            Map.of("fabricio", new Employee("Fabricio", List.of("book1", "book2"), List.of("reader")),
                    "lucas", new Employee("Fabricio", List.of("book1", "book2"), List.of("rresearcher")));

    @PostAuthorize("returnObject.roles.contains('reader')")
    public Employee getBookDetails(String name) {
        return records.get(name);
    }
}
