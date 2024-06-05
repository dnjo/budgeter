package net.dnjo.budgeter.services;

import net.dnjo.budgeter.dtos.CategoryResponse;
import net.dnjo.budgeter.dtos.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category createCategory = new Category();
        createCategory.setName(request.getName());
        return mapCategoryResponse(categoryRepository.save(createCategory));
    }

    public Optional<CategoryResponse> findCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(this::mapCategoryResponse);
    }

    public CategoryResponse updateCategory(UpdateCategoryRequest request) {
        Category updateCategory = categoryRepository.findById(request.getId())
                .orElseThrow(EntityNotFoundException::new);
        updateCategory.setName(request.getName());
        return mapCategoryResponse(categoryRepository.save(updateCategory));
    }

    public void deleteCategoryById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        categoryRepository.deleteById(id);
    }

    private CategoryResponse mapCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
