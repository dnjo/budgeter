package net.dnjo.budgeter.services;

import net.dnjo.budgeter.dtos.CreateExpenseDefinitionRequest;
import net.dnjo.budgeter.dtos.ExpenseDefinitionResponse;
import net.dnjo.budgeter.dtos.UpdateExpenseDefinitionRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.models.ExpenseDefinition;
import net.dnjo.budgeter.repositories.CategoryRepository;
import net.dnjo.budgeter.repositories.ExpenseDefinitionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static net.dnjo.budgeter.EntityDtoMapper.mapExpenseDefinitionResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseDefinitionServiceTest {

    @Mock
    private ExpenseDefinitionRepository expenseDefinitionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ExpenseDefinitionService expenseDefinitionService;

    @Test
    public void createExpenseDefinition() {
        var category = new Category(1L, "Housing");
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(categoryRepository.existsById(1L)).thenReturn(true);

        var createdExpenseDefinition = new ExpenseDefinition();
        createdExpenseDefinition.setId(1L);
        createdExpenseDefinition.setName("Rent");
        createdExpenseDefinition.setCategory(category);
        createdExpenseDefinition.setAmount(BigDecimal.valueOf(1000L));
        when(expenseDefinitionRepository.save(any())).thenReturn(createdExpenseDefinition);

        var request = new CreateExpenseDefinitionRequest(
                category.getId(),
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        ExpenseDefinitionResponse response = expenseDefinitionService.createExpenseDefinition(request);

        ExpenseDefinitionResponse expectedResponse = mapExpenseDefinitionResponse(createdExpenseDefinition);
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    public void createExpenseDefinition_categoryNotFound_throwsException() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        var request = new CreateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(()-> expenseDefinitionService.createExpenseDefinition(request));
    }

    @Test
    public void findExpenseDefinitionById() {
        var category = new Category(1L, "Housing");
        var foundExpenseDefinition = new ExpenseDefinition();
        foundExpenseDefinition.setId(1L);
        foundExpenseDefinition.setName("Rent");
        foundExpenseDefinition.setCategory(category);
        foundExpenseDefinition.setAmount(BigDecimal.valueOf(1000L));
        when(expenseDefinitionRepository.findById(1L)).thenReturn(Optional.of(foundExpenseDefinition));

        Optional<ExpenseDefinitionResponse> response = expenseDefinitionService.findExpenseDefinitionById(1L);

        ExpenseDefinitionResponse expectedResponse = mapExpenseDefinitionResponse(foundExpenseDefinition);
        assertThat(response).usingRecursiveComparison().isEqualTo(Optional.of(expectedResponse));
        verify(expenseDefinitionRepository).findById(1L);
    }

    @Test
    public void updateExpenseDefinition() {
        var category = new Category(1L, "Housing");
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);
        when(categoryRepository.existsById(1L)).thenReturn(true);

        var updatedExpenseDefinition = new ExpenseDefinition();
        updatedExpenseDefinition.setId(1L);
        updatedExpenseDefinition.setName("Rent");
        updatedExpenseDefinition.setCategory(category);
        updatedExpenseDefinition.setAmount(BigDecimal.valueOf(1000L));
        when(expenseDefinitionRepository.findById(1L)).thenReturn(Optional.of(updatedExpenseDefinition));
        when(expenseDefinitionRepository.save(any())).thenReturn(updatedExpenseDefinition);

        var request = new UpdateExpenseDefinitionRequest(
                category.getId(),
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        ExpenseDefinitionResponse response = expenseDefinitionService.updateExpenseDefinition(1L, request);

        ExpenseDefinitionResponse expectedResponse = mapExpenseDefinitionResponse(updatedExpenseDefinition);
        assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @Test
    public void updateExpenseDefinition_categoryNotFound_throwsException() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        var request = new UpdateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> expenseDefinitionService.updateExpenseDefinition(1L, request));
    }

    @Test
    public void updateExpenseDefinition_expenseDefinitionNotFound_throwsException() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(expenseDefinitionRepository.findById(1L)).thenReturn(Optional.empty());

        var request = new UpdateExpenseDefinitionRequest(
                1L,
                BigDecimal.valueOf(1000),
                "Rent",
                null);
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> expenseDefinitionService.updateExpenseDefinition(1L, request));
    }

    @Test
    public void deleteExpenseDefinitionById() {
        when(expenseDefinitionRepository.existsById(1L)).thenReturn(true);

        expenseDefinitionService.deleteExpenseDefinitionById(1L);

        verify(expenseDefinitionRepository).deleteById(1L);
    }

    @Test
    public void deleteExpenseDefinitionById_expenseDefinitionNotFound_throwsException() {
        when(expenseDefinitionRepository.existsById(1L)).thenReturn(false);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> expenseDefinitionService.deleteExpenseDefinitionById(1L));
    }

}