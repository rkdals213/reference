package com.reference.model.requestBodies

import com.reference.model.entity.Album
import com.reference.model.entity.Book
import com.reference.model.entity.Category
import com.reference.model.entity.Movie

class SelectItemRequest {
    var dType: DType = DType.ALL
    var keyword: String = ""
    var lowPrice: Int = 0
    var highPrice: Int = 0
}

enum class DType(
    val value: String
) {
    MOVIE("M"),
    ALBUM("A"),
    BOOK("B"),
    ALL("A");

    companion object {
        fun of(name: String) = valueOf(name.toUpperCase())
    }
}

class CreateItemRequest {
    var name: String = ""
    var price = 0
    var stockQuantity = 0
    var category: DType = DType.ALL
    var artist: String = ""
    var etc: String = ""
    var author: String = ""
    var isbn: String = ""
    var director: String = ""
    var actor: String = ""
}

fun createMovie(createItemRequest: CreateItemRequest): Movie {
    val movie = Movie()
    movie.name = createItemRequest.name
    movie.price = createItemRequest.price
    movie.stockQuantity = createItemRequest.stockQuantity
    movie.actor = createItemRequest.actor
    movie.director = createItemRequest.director

    val category = Category()
    category.name = createItemRequest.category.name
    movie.categories.add(Category())
    return movie
}

fun createBook(createItemRequest: CreateItemRequest): Book {
    val book = Book()
    book.name = createItemRequest.name
    book.price = createItemRequest.price
    book.stockQuantity = createItemRequest.stockQuantity
    book.author = createItemRequest.author
    book.isbn = createItemRequest.isbn

    val category = Category()
    category.name = createItemRequest.category.name
    book.categories.add(category)
    return book
}

fun createAlbum(createItemRequest: CreateItemRequest): Album {
    val album = Album()
    album.name = createItemRequest.name
    album.price = createItemRequest.price
    album.stockQuantity = createItemRequest.stockQuantity
    album.artist = createItemRequest.artist
    album.etc = createItemRequest.etc

    val category = Category()
    category.name = createItemRequest.category.name
    album.categories.add(category)
    return album
}