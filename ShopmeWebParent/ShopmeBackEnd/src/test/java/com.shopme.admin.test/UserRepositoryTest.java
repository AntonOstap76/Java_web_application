package com.shopme.admin.test;

import com.shopme.admin.repository.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateNewUserOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);

        User userAnOs = new User("antonostap@gmail.com", "passWord2023", "Anton", "Ostapchuk");
        userAnOs.addRole(roleAdmin);

       User savedUser= userRepository.save(userAnOs);
       assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewUserTwoRole(){


        User userAnOs = new User("sergiiostap1@gmail.com", "GoalWord2023", "Sergey", "Ostapchuk");
        Role roleEditor=new Role(3);
        Role roleAssistant=new Role(5);
        userAnOs.addRole(roleEditor);
        userAnOs.addRole(roleAssistant);

        User savedUser= userRepository.save(userAnOs);
        assertThat(userAnOs.getId()).isGreaterThan(0);



    }

    @Test
    public void testListAllUsers(){
        Iterable<User> userList= userRepository.findAll();
        userList.forEach(user -> System.out.println(user));

    }
    @Test
    public void testGetUserById(){
        User userAn=userRepository.findById(1).get();
        System.out.println(userAn);
        assertThat(userAn).isNotNull();
    }
    @Test
    public void testUpdateUserDetails(){
        User userAn=userRepository.findById(1).get();
        userAn.setEnabled(true);
        userAn.setEmail("antonioostap4@gmail.com");
        userRepository.save(userAn);
    }
    @Test
    public void testUpdateUserRoles() {
        User userSer=userRepository.findById(2).get();
        Role roleEditor=new Role(3);
        Role roleSalePerson=new Role(2);
        userSer.getRoles().remove(roleEditor);
        userSer.addRole(roleSalePerson);

        userRepository.save(userSer);
    }
    @Test
    public void testDeleteUser(){
        Integer userId=2;
        userRepository.deleteById(userId);



    }

    @Test
    public void getUserByEmail(){
        String email="dedd@gmail.com";
        User userByEmail = userRepository.getUserByEmail(email);

        assertThat(userByEmail).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id=3;
        Long countById=userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }
    @Test
    public void testDisableUser(){
        Integer id=3;
        userRepository.updateEnabledStatus(id, false);
    }
    @Test
    public void testEnableUser(){
        Integer id=3;
        userRepository.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 1;
        int pageSize = 4;
        Pageable pageable =  PageRequest.of(pageNumber,pageSize);
        Page<User>page = userRepository.findAll( pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user-> System.out.println(user));

        assertThat(listUsers.size()).isEqualTo(pageSize);
    }
    @Test
    public void testSearchUsers(){
        String keyword = "bruce";

        int pageNumber = 0;
        int pageSize = 4;
        Pageable pageable =  PageRequest.of(pageNumber,pageSize);
        Page<User>page = userRepository.findAll(keyword, pageable);
        List<User> listUsers = page.getContent();
        listUsers.forEach(user-> System.out.println(user));

        assertThat(listUsers.size()).isGreaterThan(0);
    }
}
