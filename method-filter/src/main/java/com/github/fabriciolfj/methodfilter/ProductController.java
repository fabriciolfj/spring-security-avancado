package com.github.fabriciolfj.methodfilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    protected ProductService productService;

    @GetMapping("/find")
    public List<Product> findAll() {
        return productService.findProducts();
    }

    @GetMapping("/sell")
    public List<Product> sellProduct() {
        var products = new ArrayList();
        products.add(new Product("bebida", "fabricio"));
        products.add(new Product("leite", "fabricio"));
        products.add(new Product("chocolate", "lucas"));
        return productService.sellProducts(products);
    }
}
