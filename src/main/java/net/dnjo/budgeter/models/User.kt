package net.dnjo.budgeter.models

import jakarta.persistence.*

@Entity
@Table(name = "users_account")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var role: String
)
