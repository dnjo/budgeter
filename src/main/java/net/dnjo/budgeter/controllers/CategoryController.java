package net.dnjo.budgeter.controllers;

import net.dnjo.budgeter.dtos.category.CategoryResponse;
import net.dnjo.budgeter.dtos.category.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.category.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.findAllCategories();
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Long id) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
