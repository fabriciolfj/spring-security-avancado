package com.github.fabriciolfj.appexemplo1.service;

import com.github.fabriciolfj.appexemplo1.entity.Product;
import com.github.fabriciolfj.appexemplo1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
