package net.dnjo.budgeter.models

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

class Expense(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(nullable = false)
    var paid: Boolean,

    @Column(nullable = false)
    var notes: String
)