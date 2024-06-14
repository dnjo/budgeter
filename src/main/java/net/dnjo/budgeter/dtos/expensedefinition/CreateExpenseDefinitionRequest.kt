package net.dnjo.budgeter.dtos.expensedefinition

import java.math.BigDecimal

class CreateExpenseDefinitionRequest(
    var categoryId: Long,

    var amount: BigDecimal,

    var name: String,

    var description: String?
)