package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.models.ExpenseDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
class ExpenseDefinitionRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExpenseDefinitionRepository expenseDefinitionRepository;

    private Category savedCategory;

    @BeforeEach
    public void setUp() {
        var category = new Category();
        category.setName("Housing");
        savedCategory = categoryRepository.save(category);
    }

    @Test
    public void saveExpenseDefinition() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        ExpenseDefinition savedDefinition = expenseDefinitionRepository.save(expenseDefinition);

        assertThat(savedDefinition).isNotNull();
        assertThat(savedDefinition.getId()).isNotNull();
        assertThat(savedDefinition.getCategory()).usingRecursiveComparison().isEqualTo(savedCategory);
        assertThat(savedDefinition.getName()).isEqualTo("Rent");
        assertThat(savedDefinition.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testSaveExpenseDefinition_missingCategory_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void testSaveExpenseDefinition_missingName_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void testSaveExpenseDefinition_missingAmount_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void testFindExpenseDefinitionById() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        ExpenseDefinition savedDefinition = expenseDefinitionRepository.save(expenseDefinition);
        ExpenseDefinition foundDefinition = expenseDefinitionRepository.findById(savedDefinition.getId())
                .orElse(null);

        assertThat(foundDefinition).isNotNull();
        assertThat(foundDefinition.getId()).isNotNull();
        assertThat(foundDefinition.getCategory()).usingRecursiveComparison().isEqualTo(savedCategory);
        assertThat(foundDefinition.getName()).isEqualTo("Rent");
        assertThat(savedDefinition.getAmount()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    public void testUpdateExpenseDefinition() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        ExpenseDefinition savedDefinition = expenseDefinitionRepository.save(expenseDefinition);
        savedDefinition.setAmount(BigDecimal.valueOf(2000));
        expenseDefinitionRepository.save(savedDefinition);
        ExpenseDefinition updatedDefinition = expenseDefinitionRepository.findById(savedDefinition.getId())
                .orElse(null);

        assertThat(updatedDefinition).isNotNull();
        assertThat(savedDefinition.getAmount()).isEqualTo(BigDecimal.valueOf(2000));
    }

    @Test
    public void testDeleteExpenseDefinitionById() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        ExpenseDefinition savedDefinition = expenseDefinitionRepository.save(expenseDefinition);
        expenseDefinitionRepository.deleteById(savedDefinition.getId());
        ExpenseDefinition deletedDefinition = expenseDefinitionRepository.findById(savedDefinition.getId())
                .orElse(null);

        assertThat(deletedDefinition).isNull();
    }

}