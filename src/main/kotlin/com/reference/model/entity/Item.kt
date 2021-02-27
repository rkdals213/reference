package com.reference.model.entity

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long = 0
    var name: String = ""
    var price = 0
    var stockQuantity = 0

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    var categories: MutableSet<Category> = HashSet()
    var deleted = false
}

@Entity
@DiscriminatorValue("A")
class Album : Item() {
    var artist: String = ""
    var etc: String = ""
}

@Entity
@DiscriminatorValue("B")
class Book : Item() {
    var author: String = ""
    var isbn: String = ""
}

@Entity
@DiscriminatorValue("M")
class Movie : Item() {
    var director: String = ""
    var actor: String = ""
}