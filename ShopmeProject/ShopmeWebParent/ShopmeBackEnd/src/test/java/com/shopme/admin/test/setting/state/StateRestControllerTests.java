package com.shopme.admin.test.setting.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.repository.setting.country.CountryRepository;
import com.shopme.admin.repository.setting.state.StateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    StateRepository stateRepository;


    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testListByCountries() throws Exception {
        Integer countryId = 1;
        String url = "/states/list_by_country/" + countryId;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get(url)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        State[] states = objectMapper.readValue(jsonResponse, State[].class);

        assertThat(states).hasSizeGreaterThan(1);
    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testCreateState() throws Exception {
        String url = "/states/save";
        Integer countryId =4;
        Country country = countryRepository.findById(countryId).get();
        State state = new State("Lower Silesia", country);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer stateId =Integer.parseInt(response);
        Optional<State> findById = stateRepository.findById(stateId);
        assertThat(findById.isPresent());

    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testUpdateState() throws Exception {
        String url = "/states/save";
        Integer stateId=3;
        String name = "California";

        State state = stateRepository.findById(stateId).get();
        state.setName(name);

        mockMvc.perform(MockMvcRequestBuilders.post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(state))
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(stateId)));

        Optional<State> findById = stateRepository.findById(stateId);

        assertThat(findById).isPresent();

        State updatedState = stateRepository.findById(stateId).get();
        assertThat(updatedState.getName()).isEqualTo(name);


    }

    @Test
    @WithMockUser(username = "nam@codejava.net", password = "nam2020", roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId=3;
        String url = "/states/delete/"+stateId;

        mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());

        Optional<State> findById = stateRepository.findById(stateId);

        assertThat(findById).isNotPresent();

    }





}
