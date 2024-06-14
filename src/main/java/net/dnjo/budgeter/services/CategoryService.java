package net.dnjo.budgeter.services;

import net.dnjo.budgeter.EntityDtoMapper;
import net.dnjo.budgeter.dtos.category.CategoryResponse;
import net.dnjo.budgeter.dtos.category.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.category.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static net.dnjo.budgeter.EntityDtoMapper.mapCategoryResponse;

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
        return category.map(EntityDtoMapper::mapCategoryResponse);
    }

    public List<CategoryResponse> findAllCategories() {
        return categoryRepository.findAll().stream()
                .map(EntityDtoMapper::mapCategoryResponse)
                .toList();
    }

    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category updateCategory = categoryRepository.findById(id)
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

}
