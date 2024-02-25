package com.shopme;

import com.shopme.common.entity.Category;
import com.shopme.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("")
    public String viewHomePage(Model model){
//        add a list of categories to main page

        List<Category> categoryList = categoryService.listNoChildrenCategories();
        model.addAttribute("categoryList", categoryList);

        return "index";
    }
}
