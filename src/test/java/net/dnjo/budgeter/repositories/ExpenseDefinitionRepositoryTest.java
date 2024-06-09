package net.dnjo.budgeter.repositories;

import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.models.ExpenseDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.List;

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
    public void saveExpenseDefinition_missingCategory_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setName("Rent");
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void saveExpenseDefinition_missingName_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setAmount(BigDecimal.valueOf(1000));

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void saveExpenseDefinition_missingAmount_throwsException() {
        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(savedCategory);
        expenseDefinition.setName("Rent");

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> expenseDefinitionRepository.save(expenseDefinition));
    }

    @Test
    public void findExpenseDefinitionById() {
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
    public void findAllExpenseDefinitions() {
        var expenseDefinition1 = new ExpenseDefinition();
        expenseDefinition1.setCategory(savedCategory);
        expenseDefinition1.setName("Rent");
        expenseDefinition1.setAmount(BigDecimal.valueOf(1000));
        ExpenseDefinition savedDefinition = expenseDefinitionRepository.save(expenseDefinition1);
        var expenseDefinition2 = new ExpenseDefinition();
        expenseDefinition2.setCategory(savedCategory);
        expenseDefinition2.setName("Electricity");
        expenseDefinition2.setAmount(BigDecimal.valueOf(100));
        ExpenseDefinition savedDefinition2 = expenseDefinitionRepository.save(expenseDefinition2);

        List<ExpenseDefinition> foundExpenseDefinitions = expenseDefinitionRepository.findAll();

        List<ExpenseDefinition> expectedExpenseDefinitions = List.of(new ExpenseDefinition(
                        savedDefinition.getId(),
                        savedCategory,
                        null,
                        BigDecimal.valueOf(1000),
                        "Rent",
                        null),
                new ExpenseDefinition(
                        savedDefinition2.getId(),
                        savedCategory,
                        null,
                        BigDecimal.valueOf(100),
                        "Electricity",
                        null));
        assertThat(foundExpenseDefinitions).usingRecursiveComparison().isEqualTo(expectedExpenseDefinitions);
    }

    @Test
    public void updateExpenseDefinition() {
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
    public void deleteExpenseDefinitionById() {
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