package com.shopme.admin.test.user;

import com.shopme.admin.repository.user.RoleRepository;
import com.shopme.common.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testCreateFirstRole(){
        Role roleAdmin=new Role("Admin", "manage everything");
        Role savedRole=roleRepository.save(roleAdmin);
        assertThat(savedRole.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateRestRoles(){

        Role roleSalesPerson=new Role("SalesPerson", "manage product price, customers, shipping, orders" +
                "and sales report ");

        Role roleEditor=new Role("Editor", "manage categories, brands, products,  articles" +
                "and menus ");
        Role roleShipper=new Role("Shipper", "view products, view orders and update order status");

        Role roleAssistant=new Role("Assistant", "manage questions and reviews");

        roleRepository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));

    }
}
