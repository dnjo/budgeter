package net.dnjo.budgeter.models

import jakarta.persistence.*

@Entity
class Category (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String
)