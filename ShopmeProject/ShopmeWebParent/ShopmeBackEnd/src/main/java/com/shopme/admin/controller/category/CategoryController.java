package com.shopme.admin.controller.category;

import com.shopme.admin.export.category.CategoryCSVExporter;
import com.shopme.admin.files.CategoryPageInfo;
import com.shopme.admin.files.FileUploadUtil;
import com.shopme.admin.service.category.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFindExeption;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @GetMapping("/categories")
    public String listFirstPage(@Param("sortDir") String sortDir,Model model){
        return listByPage(1,sortDir, null, model);
    }


    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum,
                             @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword,
                             Model model){

        if(sortDir==null || sortDir.isEmpty()) {
            sortDir = "asc";

        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> categoryList = categoryService.listByPage(pageInfo,pageNum,sortDir,keyword);
        long startCount = (pageNum - 1) *categoryService.TOP_CATEGORIES_PER_PAGE+1;
        long endCount = startCount+categoryService.TOP_CATEGORIES_PER_PAGE-1;
        if(endCount>pageInfo.getTotalElements()){
            endCount=pageInfo.getTotalElements();
        }


        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("reverseSortDir", reverseSortDir);

        return "categories/categories";

    }


    @GetMapping("/categories/new")
    public String newCategory(Model model){

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", new Category());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");

        return "categories/category_form";
    }
    @PostMapping("/categories/save")
    public String saveCategory(Category category,
                               @RequestParam("fileImage")MultipartFile multipartFile,
                               RedirectAttributes redirectAttributes) throws IOException {
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            category.setImage(fileName);

            Category savedCategory = categoryService.save(category);

            String uploadDir = "../category-images/" + savedCategory.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else{
            categoryService.save(category);
        }

        redirectAttributes.addFlashAttribute("message", "The category has been saved successfully.");
        return "redirect:/categories";

    }
    @GetMapping("/categories/edit/{id}")
    public String updateCategories(@PathVariable(name="id")Integer id, Model model,
                                   RedirectAttributes redirectAttributes){

        try{
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category (ID: " + id + ")");
            return "categories/category_form";
        } catch (CategoryNotFindExeption ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/categories";
        }

    }
    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id")Integer id,@PathVariable("status") boolean enabled,
                                          RedirectAttributes ra){

        categoryService.updateEnabledStatus(id,enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The category ID "+id+" has been "+status;

        ra.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id")Integer id, RedirectAttributes ra, Model model){
        try{
            categoryService.delete(id);
            String categoryDir = "../category-images/"+id;
            FileUploadUtil.removeDir(categoryDir);
            ra.addFlashAttribute("message", "The category with ID "+id+" is successfully deleted");
        } catch (CategoryNotFindExeption e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException{
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();
        CategoryCSVExporter exporter = new CategoryCSVExporter();
        exporter.export(categoryList,response);
    }
}
