package com.shopme.admin.service.product;


import com.shopme.admin.repository.product.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ProductNotFindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class ProductService {

    public static final int PRODUCTS_PER_PAGE=5;
    @Autowired
    private ProductRepository productRepository;

    public List<Product> listAll(){
        return (List<Product>) productRepository.findAll();
    }
    public Product save(Product product){
        if(product.getId() == null){
            product.setCreatedTime(new Date());
        }
        if(product.getAlias() == null || product.getAlias().isEmpty()){
            String defaultAlias = product.getName().replace(" ", "-");
            product.setAlias(defaultAlias);
        }else{
            product.setAlias(product.getAlias().replace(" ", "-"));
        }
        product.setUpdatedTime(new Date());

        return productRepository.save(product);
    }

    // for saving only changes of prices for salesperson
    public void saveProductPrice(Product productInForm){
        Product productInDB = productRepository.findById(productInForm.getId()).get();
        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        productRepository.save(productInDB);

    }

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Integer categoryId) {
        Sort sort = Sort.by(sortField);

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum-1, PRODUCTS_PER_PAGE , sort);

        if (keyword != null && !keyword.isEmpty()) {
        //and also checks the presence of categoryId
            if(categoryId != null && categoryId>0){
                String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch,keyword,pageable);
            }
            return productRepository.findAll(keyword, pageable);
        }

        //checking if any category was chosen and find product by cat.id
        if(categoryId != null && categoryId>0){
            String categoryIdMatch = "-"+String.valueOf(categoryId)+"-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch,pageable);
        }

        return productRepository.findAll(pageable);
    }

    public String checkUnique(Integer id, String name){

        boolean isCreatingNew = (id==null || id==0);
        String nameTrim = name.trim();

        Product productByName = productRepository.findByName(nameTrim);

        if(isCreatingNew) {
            if (productByName != null) {
                return "Duplicate";
            }
        }else{
              if(productByName !=null && productByName.getId() != id ){
                  return "Duplicate";
            }
        }
        return "OK";
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        productRepository.updateEnabled(id,enabled);
    }
    public void deleteProduct(Integer id) throws ProductNotFindException {
        Long countById = productRepository.countById(id);
        if(countById == null || countById==0){
            throw new ProductNotFindException("Could not find any product with ID: "+id);
        }
        productRepository.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFindException{
        try {
            return productRepository.findById(id).get();
        }catch(NoSuchElementException ex){
            throw new ProductNotFindException("Could not find any product with ID "+id);
        }
    }
}
