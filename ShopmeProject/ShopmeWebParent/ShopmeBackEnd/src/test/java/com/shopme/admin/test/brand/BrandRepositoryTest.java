package com.shopme.admin.test.brand;

import com.shopme.admin.repository.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

    @Autowired
    private BrandRepository brandRepository;


    @Test
    public void testCreateBrand1(){
        Category laptops = new Category(6);
        Brand acer = new Brand("Acer");
        acer.getCategories().add(laptops);
        Brand savedBrand = brandRepository.save(acer);

         assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateBrand2(){
        Category cellPhones = new Category(4);
        Category tablets = new Category(7);
        Brand apple = new Brand("apple");
        apple.getCategories().addAll(List.of(cellPhones,tablets));

        Brand savedBrand=brandRepository.save(apple);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(1);
    }
    @Test
    public void testCreateBrand3(){
        Brand samsung = new Brand("Samsung");
        samsung.getCategories().add( new Category(29));
        samsung.getCategories().add( new Category(24));

        Brand savedBrand = brandRepository.save(samsung);

        assertThat(savedBrand).isNotNull();
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    @Test
    public void testFindAll(){
        Iterable<Brand> brandList = brandRepository.findAll();
        brandList.forEach(System.out::println);

        assertThat(brandList).isNotEmpty();

    }

    @Test
    public void testGetById(){
        Brand acer = brandRepository.findById(1).get();

        assertThat(acer.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdateBrand(){
        String newName = "Samsung Electronics";

        Brand samsung = brandRepository.findById(3).get();
        samsung.setName(newName);
        System.out.println(samsung);
        assertThat(samsung.getName()).isEqualTo(newName);
    }

    @Test
    public void testDeleteBrand(){
        Integer brandId=7;
        brandRepository.deleteById(brandId);

        Optional<Brand> deleteBrand = brandRepository.findById(brandId);

        assertThat(deleteBrand.isEmpty());
    }
}
