package com.shopme.admin.service.brand;

import com.shopme.admin.exeption.BrandNotFindExeption;
import com.shopme.admin.repository.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE=10;
    @Autowired
    BrandRepository brandRepository;

    public List<Brand> listAll() {
        return (List<Brand>) brandRepository.findAll();
    }
    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, BRAND_PER_PAGE , sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    public Brand get(Integer id) throws BrandNotFindExeption {
        try {
            return brandRepository.findById(id).get();

        } catch (NoSuchElementException ex) {
            throw new BrandNotFindExeption("Could not find brand with ID: " + id);
        }

    }

    public void delete(Integer id) throws BrandNotFindExeption {
        Long countById = brandRepository.countById(id);

        if (countById == null || countById == 0) {
            throw new BrandNotFindExeption("There is no Brand with ID " + id);
        }
        brandRepository.deleteById(id);
    }

    public String checkUnique(Integer id, String name) {

        boolean isCreatingNew = (id == null || id == 0);

        String name1=name.trim();

        Brand brandByName = brandRepository.findByName(name1);

        if (isCreatingNew) {
            if (brandByName != null) return "Duplicate";
        }else {
                if (brandByName != null && brandByName.getId() != id) {
                    return "Duplicate";
                }
            }
            return "OK";
        }




}
