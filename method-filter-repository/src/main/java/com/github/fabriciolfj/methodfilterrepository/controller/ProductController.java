package com.github.fabriciolfj.methodfilterrepository.controller;

import com.github.fabriciolfj.methodfilterrepository.entity.Product;
import com.github.fabriciolfj.methodfilterrepository.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/products/{text}")
    public List<Product> findProductsContaining(@PathVariable String text) {
        final var products = productRepository.findProductByNameContains(text);
        return products;
    }
}
