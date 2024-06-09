package net.dnjo.budgeter.models

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class ExpenseDefinition(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User?,

    @Column(nullable = false)
    var amount: BigDecimal,

    @Column(nullable = false)
    var name: String,

    @Column
    var description: String?
)