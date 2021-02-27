package com.reference.model.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.ArrayList
import javax.persistence.*

@Entity
class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    var id: Long? = null
    var name: String? = null

    @ManyToMany
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    @JsonIgnore
    var items: MutableSet<Item> = HashSet()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    var parent: Category? = null

    @OneToMany(mappedBy = "parent")
    var child: MutableList<Category> = ArrayList()

    fun addChildCategory(child: Category) {
        this.child.add(child)
        child.parent = this
    }
}