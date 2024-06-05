package net.dnjo.budgeter.services;

import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() {
        var category = new Category();
        category.setId(1L);
        category.setName("Shopping");

        when(categoryRepository.save(eq(category))).thenReturn(category);

        Category createdCategory = categoryService.createCategory(category);

        assertThat(createdCategory).isEqualTo(category);
        verify(categoryRepository).save(category);
    }

    @Test
    public void testFindCategoryById() {
        var category = new Category();
        category.setId(1L);
        category.setName("Shopping");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> foundCategory = categoryService.findCategoryById(1L);

        assertThat(foundCategory).isEqualTo(Optional.of(category));
        verify(categoryRepository).findById(1L);
    }

    @Test
    public void testUpdateCategory() {
        var category = new Category();
        category.setId(1L);
        category.setName("Shopping");

        when(categoryRepository.save(eq(category))).thenReturn(category);

        Category updatedCategory = categoryService.updateCategory(category);

        assertThat(updatedCategory).isEqualTo(category);
        verify(categoryRepository).save(category);
    }

    @Test
    public void testDeleteCategory() {
        categoryService.deleteCategoryById(1L);

        verify(categoryRepository).deleteById(1L);
    }

}