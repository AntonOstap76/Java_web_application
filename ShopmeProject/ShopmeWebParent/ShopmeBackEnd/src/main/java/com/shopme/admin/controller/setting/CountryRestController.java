package com.shopme.admin.controller.setting;

import com.shopme.admin.repository.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryRestController {

    @Autowired
    private CountryRepository countryRepository;

    @GetMapping("/countries/list")
    public List<Country> listAll(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    @PostMapping("/countries/save")
                        //requestBody converting json code to java code
    public String save(@RequestBody Country country){
        Country savedCountry = countryRepository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @GetMapping("/countries/delete/{id}")
    public void delete(@PathVariable("id") Integer id){
        countryRepository.deleteById(id);
    }


}
