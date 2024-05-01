package com.shopme.admin.test.setting.state;

import com.shopme.admin.repository.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTests {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCreateState(){
        Integer countryId = 3;
        Country country = entityManager.find(Country.class, countryId);

        State state = stateRepository.save(new State("Chicago", country));

        assertThat(state).isNotNull();
        assertThat(state.getId()).isGreaterThan(0);
    }

    @Test
    public void testListStatesByCountry(){
        Integer countryId = 1;
        Country country = entityManager.find(Country.class, countryId);
        List<State> stateList = stateRepository.findByCountryOrderByNameAsc(country);

        stateList.forEach(System.out::println);

        assertThat(stateList.size()).isGreaterThan(0);
    }

    @Test
    public void testUpdateStates(){
        Integer stateId = 3;
        String nameState = "Illinois";

        State state = stateRepository.findById(stateId).get();

        state.setName(nameState);

        State save = stateRepository.save(state);
        assertThat(save.getName()).isEqualTo(nameState);
    }

    @Test
    public void testGetState(){

        Integer stateId=1;

        Optional<State> state = stateRepository.findById(stateId);
        assertThat(state.isPresent());
    }

    @Test
    public void testDeleteState(){
        Integer stateId=4;
        stateRepository.deleteById(stateId);

        Optional<State> state = stateRepository.findById(stateId);

        assertThat(state.isEmpty());
    }






}
