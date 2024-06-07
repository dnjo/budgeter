package net.dnjo.budgeter.controllers;

import net.dnjo.budgeter.dtos.CategoryResponse;
import net.dnjo.budgeter.dtos.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponse createCategory(@RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable("id") Long id) {
        Optional<CategoryResponse> category = categoryService.findCategoryById(id);
        if (category.isPresent()) {
            return category.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public CategoryResponse updateCategory(@PathVariable("id") Long id, @RequestBody UpdateCategoryRequest request) {
        try {
            return categoryService.updateCategory(id, request);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
