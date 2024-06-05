package net.dnjo.budgeter.services;

import net.dnjo.budgeter.dtos.CategoryResponse;
import net.dnjo.budgeter.dtos.CreateCategoryRequest;
import net.dnjo.budgeter.dtos.UpdateCategoryRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() {
        var createdCategory = new Category(1L, "Shopping");
        when(categoryRepository.save(any())).thenReturn(createdCategory);

        var request = new CreateCategoryRequest("Shopping");
        CategoryResponse response = categoryService.createCategory(request);

        CategoryResponse expectedResponse = new CategoryResponse(1L, "Shopping");
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
        verify(categoryRepository).save(any());
    }

    @Test
    public void testFindCategoryById() {
        var foundCategory = new Category(1L, "Shopping");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(foundCategory));

        Optional<CategoryResponse> response = categoryService.findCategoryById(1L);

        CategoryResponse expectedResponse = new CategoryResponse(1L, "Shopping");
        assertThat(response).usingRecursiveComparison().isEqualTo(Optional.of(expectedResponse));
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void testUpdateCategory() {
        var updatedCategory = new Category(1L, "Shopping");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(updatedCategory));
        when(categoryRepository.save(any())).thenReturn(updatedCategory);

        var request = new UpdateCategoryRequest(1L, "Shopping");
        CategoryResponse response = categoryService.updateCategory(request);

        CategoryResponse expectedResponse = new CategoryResponse(1L, "Shopping");
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
        verify(categoryRepository).save(any());
    }

    @Test
    public void testUpdateCategory_categoryNotFound_throwsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        var request = new UpdateCategoryRequest(1L, "Shopping");
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> categoryService.updateCategory(request));
    }

    @Test
    public void testDeleteCategory() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_categoryNotFound_throwsException() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> categoryService.deleteCategoryById(1L));
    }

}