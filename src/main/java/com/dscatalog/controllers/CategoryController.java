package com.dscatalog.controllers;

import com.dscatalog.entities.Category;
import com.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;
    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        List<Category> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

}