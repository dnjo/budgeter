package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void saveCategory() {
        var category = new Category();
        category.setName("Shopping");

        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Shopping");
    }

    @Test
    public void findCategoryById() {
        var category = new Category();
        category.setName("Shopping");
        Category savedCategory = categoryRepository.save(category);

        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Shopping");
    }

    @Test
    public void findAllCategories() {
        var category1 = new Category();
        category1.setName("Shopping");
        Category savedCategory1 = categoryRepository.save(category1);
        var category2 = new Category();
        category2.setName("Transportation");
        Category savedCategory2 = categoryRepository.save(category2);

        List<Category> foundCategories = categoryRepository.findAll();

        List<Category> expectedCategories = List.of(
                new Category(savedCategory1.getId(), "Shopping"),
                new Category(savedCategory2.getId(), "Transportation"));
        assertThat(foundCategories).usingRecursiveComparison().isEqualTo(expectedCategories);
    }

    @Test
    public void updateCategory() {
        var category = new Category();
        category.setName("Shopping");
        Category savedCategory = categoryRepository.save(category);

        savedCategory.setName("Transportation");
        categoryRepository.save(savedCategory);
        Category updatedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("Transportation");
    }

    @Test
    public void deleteCategoryById() {
        var category = new Category();
        category.setName("Shopping");
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.deleteById(savedCategory.getId());
        Category deletedCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertThat(deletedCategory).isNull();
    }

}