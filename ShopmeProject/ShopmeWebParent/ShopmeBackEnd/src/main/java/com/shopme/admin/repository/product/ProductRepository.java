package com.shopme.admin.repository.product;


import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends CrudRepository<Product,Integer>, PagingAndSortingRepository<Product, Integer> {

    public Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled=?2 WHERE p.id=?1")
    @Modifying
    public void updateEnabled(Integer id, boolean enabled);

    public Long countById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"
            +"OR p.shortDescription LIKE %?1%"
            +"OR p.fullDescription LIKE %?1%"
            +"OR p.brand.name LIKE %?1%"
            +"OR p.category.name LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);



    //add new query for finding product where  category.id = categoryId OR p.category.allParentsIds have this id in it
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 "
            + "OR p.category.allParentIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Integer categoryId, String categoryIdMatch, Pageable pageable);


    //to find product with keyword included in  category and subcategories
    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 "
            + "OR p.category.allParentIDs LIKE %?2%) AND "
            +"(p.name LIKE %?3%"
            +"OR p.shortDescription LIKE %?3%"
            +"OR p.fullDescription LIKE %?3%"
            +"OR p.brand.name LIKE %?3%"
            +"OR p.category.name LIKE %?3%)")
    public Page<Product> searchInCategory(Integer categoryId, String categoryIdMatch,String keyword,Pageable pageable);
}


