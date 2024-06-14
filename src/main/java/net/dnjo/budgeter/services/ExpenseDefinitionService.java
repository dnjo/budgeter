package net.dnjo.budgeter.services;

import net.dnjo.budgeter.EntityDtoMapper;
import net.dnjo.budgeter.dtos.expensedefinition.CreateExpenseDefinitionRequest;
import net.dnjo.budgeter.dtos.expensedefinition.ExpenseDefinitionResponse;
import net.dnjo.budgeter.dtos.expensedefinition.UpdateExpenseDefinitionRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.models.ExpenseDefinition;
import net.dnjo.budgeter.repositories.CategoryRepository;
import net.dnjo.budgeter.repositories.ExpenseDefinitionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static net.dnjo.budgeter.EntityDtoMapper.mapExpenseDefinitionResponse;

@Service
public class ExpenseDefinitionService {

    private final ExpenseDefinitionRepository expenseDefinitionRepository;

    private final CategoryRepository categoryRepository;

    public ExpenseDefinitionService(
            ExpenseDefinitionRepository expenseDefinitionRepository,
            CategoryRepository categoryRepository
    ) {
        this.expenseDefinitionRepository = expenseDefinitionRepository;
        this.categoryRepository = categoryRepository;
    }

    public ExpenseDefinitionResponse createExpenseDefinition(CreateExpenseDefinitionRequest request) {
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new EntityNotFoundException();
        }

        var expenseDefinition = new ExpenseDefinition();
        expenseDefinition.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        expenseDefinition.setAmount(request.getAmount());
        expenseDefinition.setName(request.getName());
        expenseDefinition.setDescription(request.getDescription());

        return mapExpenseDefinitionResponse(expenseDefinitionRepository.save(expenseDefinition));
    }

    public Optional<ExpenseDefinitionResponse> findExpenseDefinitionById(Long id) {
        return expenseDefinitionRepository.findById(id).map(EntityDtoMapper::mapExpenseDefinitionResponse);
    }

    public List<ExpenseDefinitionResponse> findAllExpenseDefinitions() {
        return expenseDefinitionRepository.findAll()
                .stream()
                .map(EntityDtoMapper::mapExpenseDefinitionResponse)
                .toList();
    }

    public ExpenseDefinitionResponse updateExpenseDefinition(Long id, UpdateExpenseDefinitionRequest request) {
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new EntityNotFoundException();
        }

        ExpenseDefinition updateExpenseDefinition = expenseDefinitionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        updateExpenseDefinition.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        updateExpenseDefinition.setAmount(request.getAmount());
        updateExpenseDefinition.setName(request.getName());
        updateExpenseDefinition.setDescription(request.getDescription());

        return mapExpenseDefinitionResponse(expenseDefinitionRepository.save(updateExpenseDefinition));
    }

    public void deleteExpenseDefinitionById(Long id) {
        if (!expenseDefinitionRepository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        expenseDefinitionRepository.deleteById(id);
    }

}
