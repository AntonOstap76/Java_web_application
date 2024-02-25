package com.shopme.tests;

import com.shopme.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.shopme.common.entity.Category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testListEnabledCategories() {
        List<Category> categoryList = categoryRepository.findAllEnabled();

        categoryList.forEach(cat -> {
            System.out.println(cat.getName() + " (" + cat.isEnabled() + ")");
        });
    }

    @Test
    public void testFindCategoryByAlias() {
        String alias = "electronics";
        Category category = categoryRepository.findByAliasEnabled(alias);

        assertThat(category).isNotNull();

    }
}
