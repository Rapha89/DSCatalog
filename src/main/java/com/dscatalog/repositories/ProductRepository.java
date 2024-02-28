package com.dscatalog.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import com.dscatalog.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
