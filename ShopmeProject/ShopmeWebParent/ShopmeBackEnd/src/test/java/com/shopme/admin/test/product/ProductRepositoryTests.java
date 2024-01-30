package com.shopme.admin.test.product;

import com.shopme.admin.repository.product.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.access.method.P;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateProduct(){
        Brand brand = entityManager.find(Brand.class, 1);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for acer aspire desktop");
        product.setFullDescription("This is a very good full description for acer aspire desktop");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(477);
        product.setCost(600);

        product.setEnabled(true);
        product.setInStock(true);

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }
    @Test
    public void testListAllProducts(){
        Iterable<Product> productsList = productRepository.findAll();

        productsList.forEach(System.out::println);
    }

    @Test
    public void testGetProduct(){
        Integer id = 2;
        Product product = productRepository.findById(id).get();

        System.out.println(product);
        assertThat(product).isNotNull();
    }

    @Test
    public void testUpdateProduct(){
        Integer id=1;
        Product product = productRepository.findById(id).get();
        product.setPrice(499);

        productRepository.save(product);

       Product updatedProduct = entityManager.find(Product.class, id);

       assertThat(updatedProduct.getPrice()).isEqualTo(499);

    }

    @Test
    public void testDeleteProduct(){
        Integer id = 3;

        productRepository.deleteById(id);

        Optional<Product> product = productRepository.findById(id);

        assertThat(!product.isPresent());

    }

    @Test
    public void testSaveProductWithImages(){
        Integer id = 1;
        Product product = productRepository.findById(id).get();

        product.setMainImage("main_image.jpg");
        product.addExtraImage("extra images 1.png");
        product.addExtraImage("extra_images_2.png");
        product.addExtraImage("extra-images3.png");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails(){
        Integer productId=1;
        Product product = productRepository.findById(productId).get();

        product.addDetail("Device Memory" , "128G");
        product.addDetail("CPU Model" , "MediaTek");
        product.addDetail("OS" , "Android 10");

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getDetails()).isNotEmpty();
    }
}
