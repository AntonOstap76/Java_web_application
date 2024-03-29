package com.shopme.admin.controller.user;

import com.shopme.admin.exeption.UserNotFindExeption;
import com.shopme.admin.export.user.UserCsvExporter;
import com.shopme.admin.export.user.UserExcelExporter;
import com.shopme.admin.export.user.UserPDFExporter;
import com.shopme.admin.files.FileUploadUtil;
import com.shopme.admin.service.user.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listFirstPage(Model model) {


        return listByPage(1,model,"firstName","asc", null);
    }
    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                             @Param("sortField") String sortField, @Param("sortDir") String sortDir,
                             @Param("keyword") String keyword){

        Page<User> page=userService.listByPage(pageNum,sortField,sortDir,keyword);
        List<User> listUsers = page.getContent();


        System.out.println("Sort field  = "+sortField);
        System.out.println("Sort order  = "+sortDir);
        System.out.println("pageNum= "+pageNum);
        System.out.println("Total Element "+page.getTotalElements());
        System.out.println("Total Pages "+page.getTotalPages());

        long startCount = (pageNum-1)*userService.USER_PER_PAGE+1;
        long endCount = startCount+userService.USER_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);

        return "users/users";
    }


    @GetMapping("/users/new")
    public String newUser(Model model) {

        List<Role> listRoles = userService.listRoles();
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");


        return "/users/user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image")MultipartFile multipartFile) throws IOException {
        System.out.println(user);
        System.out.println(multipartFile.getOriginalFilename());

        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            User savedUser = userService.save(user);
            String uploadDir = "user-photos/"+savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
             userService.save(user);
        }
         redirectAttributes.addFlashAttribute("message", "The user has been saved successfully");
        return getRedirectURLToAffectedUser(user);
    }

    private static String getRedirectURLToAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           RedirectAttributes redirectAttributes, Model model) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");

            return "users/user_form";
        } catch (UserNotFindExeption ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }
    }
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id,
                           RedirectAttributes redirectAttributes) {
        try {
             userService.delete(id);
             redirectAttributes.addFlashAttribute("message", "The user ID "+id+" has been deleted successfully");
        } catch (UserNotFindExeption ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());

        }
        return "redirect:/users";
    }
    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes){

        userService.updateUserEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The user ID " + id + " has been "+status;

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/users";
    }
    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserCsvExporter userCsvExporter = new UserCsvExporter();
        userCsvExporter.export(listUsers, response);
    }
    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserExcelExporter userExcelExporter = new UserExcelExporter();
        userExcelExporter.export(listUsers,response);

    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserPDFExporter userPDFExporter = new UserPDFExporter();
        userPDFExporter.export(listUsers,response);
    }

}




