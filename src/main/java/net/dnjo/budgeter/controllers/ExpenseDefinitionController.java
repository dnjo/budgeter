package net.dnjo.budgeter.controllers;

import net.dnjo.budgeter.dtos.expensedefinition.CreateExpenseDefinitionRequest;
import net.dnjo.budgeter.dtos.expensedefinition.ExpenseDefinitionResponse;
import net.dnjo.budgeter.dtos.expensedefinition.UpdateExpenseDefinitionRequest;
import net.dnjo.budgeter.exceptions.EntityNotFoundException;
import net.dnjo.budgeter.services.ExpenseDefinitionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("expense-definitions")
public class ExpenseDefinitionController {
    private final ExpenseDefinitionService expenseDefinitionService;

    public ExpenseDefinitionController(ExpenseDefinitionService expenseDefinitionService) {
        this.expenseDefinitionService = expenseDefinitionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseDefinitionResponse createExpenseDefinition(@RequestBody CreateExpenseDefinitionRequest request) {
        return expenseDefinitionService.createExpenseDefinition(request);
    }

    @GetMapping("/{id}")
    public ExpenseDefinitionResponse getExpenseDefinition(@PathVariable Long id) {
        Optional<ExpenseDefinitionResponse> expenseDefinition = expenseDefinitionService.findExpenseDefinitionById(id);
        if (expenseDefinition.isPresent()) {
            return expenseDefinition.get();
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<ExpenseDefinitionResponse> getAllExpenseDefinitions() {
        return expenseDefinitionService.findAllExpenseDefinitions();
    }

    @PutMapping("/{id}")
    public ExpenseDefinitionResponse updateExpenseDefinition(
            @PathVariable Long id,
            @RequestBody UpdateExpenseDefinitionRequest request
    ) {
        try {
            return expenseDefinitionService.updateExpenseDefinition(id, request);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpenseDefinition(@PathVariable Long id) {
        try {
            expenseDefinitionService.deleteExpenseDefinitionById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
