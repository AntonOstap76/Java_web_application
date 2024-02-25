package com.shopme.repository;

import com.shopme.common.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer> {


//    Bring every enabled category
    @Query("SELECT c FROM Category c WHERE c.enabled = true ORDER BY c.name ASC")
    public List<Category> findAllEnabled();

    // bring every enabled category with the same alias from db
    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.alias=?1")
    public Category findByAliasEnabled(String alias);
}
