package net.dnjo.budgeter.dtos.expensedefinition

import net.dnjo.budgeter.dtos.category.CategoryResponse
import java.math.BigDecimal

class ExpenseDefinitionResponse(
    var id: Long,

    var category: CategoryResponse,

    var amount: BigDecimal,

    var name: String,

    var description: String?
)