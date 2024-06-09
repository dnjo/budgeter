package net.dnjo.budgeter.dtos

import java.math.BigDecimal

class UpdateExpenseDefinitionRequest(
    var categoryId: Long,

    var amount: BigDecimal,

    var name: String,

    var description: String?
)