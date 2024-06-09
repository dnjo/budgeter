package net.dnjo.budgeter;

import net.dnjo.budgeter.dtos.CategoryResponse;
import net.dnjo.budgeter.dtos.ExpenseDefinitionResponse;
import net.dnjo.budgeter.models.Category;
import net.dnjo.budgeter.models.ExpenseDefinition;

public class EntityDtoMapper {

    public static CategoryResponse mapCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

    public static ExpenseDefinitionResponse mapExpenseDefinitionResponse(ExpenseDefinition expenseDefinition) {
        return new ExpenseDefinitionResponse(
                expenseDefinition.getId(),
                mapCategoryResponse(expenseDefinition.getCategory()),
                expenseDefinition.getAmount(),
                expenseDefinition.getName(),
                expenseDefinition.getDescription());
    }
}
