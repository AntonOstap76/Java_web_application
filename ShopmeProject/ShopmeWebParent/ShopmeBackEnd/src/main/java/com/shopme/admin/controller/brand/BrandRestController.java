package com.shopme.admin.controller.brand;

import com.shopme.admin.exeption.BrandNotFindExeption;
import com.shopme.admin.exeption.BrandNotFindRestException;
import com.shopme.admin.service.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    @Autowired
    BrandService brandService;

    @PostMapping("/brands/check_unique")
    public String checkUnique(@Param("id")Integer id ,
                              @Param("name")String name){
        return brandService.checkUnique(id,name);
    }
    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id")Integer brandId) throws  BrandNotFindRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();
        try{
            Brand brand = brandService.get(brandId);
            Set<Category> categories = brand.getCategories();

            for(Category category : categories){
                CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(dto);
            }
            return listCategories;
        }catch(BrandNotFindExeption ex){
            throw new BrandNotFindRestException();
        }
    }
}
