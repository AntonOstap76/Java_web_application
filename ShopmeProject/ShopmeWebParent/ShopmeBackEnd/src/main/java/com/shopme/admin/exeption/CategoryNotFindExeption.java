package com.shopme.admin.exeption;

import com.shopme.common.entity.Category;

public class CategoryNotFindExeption extends Exception {
    public CategoryNotFindExeption(String message) {
        super(message);
    }
}
