package org.example.repository;

import org.example.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p INNER JOIN p.brand")
    public List<Product> findAllWithBrand();

    @Query("SELECT p FROM Product p INNER JOIN p.brand b where b.id = :id")
    public List<Product>findAllProductsByBrandId(@Param("id") Long id);

    @Query("SELECT p FROM Product p INNER JOIN p.brand b " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT(:search,'%') ) " +
            "OR LOWER(b.name) LIKE LOWER(CONCAT(:search,'%'))")
    public List<Product>findAllSearchProducts(@Param("search") String search);
}
