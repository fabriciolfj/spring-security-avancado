package com.github.fabriciolfj.methodfilterrepository.repository;

import com.github.fabriciolfj.methodfilterrepository.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:text% AND p.owner=?#{authentication.principal.username}")
    //@PostFilter("filterObject.owner == authentication.principal.username")
    List<Product> findProductByNameContains(String text);
}
