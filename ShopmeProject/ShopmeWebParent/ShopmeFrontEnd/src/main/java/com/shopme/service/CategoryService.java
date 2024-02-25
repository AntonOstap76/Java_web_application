package com.shopme.service;

import com.shopme.common.entity.Category;
import com.shopme.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChildrenCategories() {
        List<Category> noChildrenCategories = new ArrayList<>();
        List<Category> enabledCategories = categoryRepository.findAllEnabled();

        enabledCategories.forEach(cat -> {
            Set<Category> children = cat.getChildren();
            if(children == null || children.size()==0){
                noChildrenCategories.add(cat);
            }
        });

        return noChildrenCategories;
    }

    //get category from category repository
    public Category getCategory(String alias){
        return categoryRepository.findByAliasEnabled(alias);
    }

    public List<Category> getCategoryParents(Category child){
        List<Category> listParents = new ArrayList<>();

        Category parent = child.getParent();

        // add parent to parentList
        while(parent != null){
            listParents.add(0, parent);
            parent = parent.getParent();
        }

         listParents.add(child);

        return listParents;
    }
}
