package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    public void findById() {
        var category = new Category();
        category.setName("Shopping");
        Category savedCategory = categoryRepository.save(category);

        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElse(null);

        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getName()).isEqualTo("Shopping");
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