package com.github.fabriciolfj.appexemplo1.repository;

import com.github.fabriciolfj.appexemplo1.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
