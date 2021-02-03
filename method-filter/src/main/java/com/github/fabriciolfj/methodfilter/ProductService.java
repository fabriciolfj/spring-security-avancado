package com.github.fabriciolfj.methodfilter;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @PreFilter("filterObject.owner == authentication.name")
    public List<Product> sellProducts(List<Product> products) {
        return products;
    }

    @PostFilter("filterObject.owner == authentication.name")
    public List<Product> findProducts() {
        var products = new ArrayList();
        products.add(new Product("bebida", "fabricio"));
        products.add(new Product("leite", "fabricio"));
        products.add(new Product("chocolate", "lucas"));
        return products;
    }
}
