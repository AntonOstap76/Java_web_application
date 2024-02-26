package com.shopme.repository;

import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


// for listing products in homepage
public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled=true " +
            "AND (p.category.id=?1 OR p.category.allParentIDs LIKE %?2%)"+
             "ORDER BY p.name ASC")
    public Page<Product> listByCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);

    public Product findByAlias(String alias);
}
