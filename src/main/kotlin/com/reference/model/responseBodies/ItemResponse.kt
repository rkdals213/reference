package com.reference.model.responseBodies

import com.reference.model.entity.Album
import com.reference.model.entity.Book
import com.reference.model.entity.Movie
import org.springframework.data.domain.Page

open class ItemDto {
    var id: Long = 0
    var name: String = ""
    var price = 0
    var stockQuantity = 0
}

class AlbumDto(album: Album) : ItemDto() {
    var artist: String = ""
    var etc: String = ""

    init {
        this.id = album.id
        this.name = album.name
        this.price = album.price
        this.stockQuantity = album.stockQuantity
        this.artist = album.artist
        this.etc = album.etc
    }
}

class MovieDto(movie: Movie) : ItemDto() {
    var director: String = ""
    var actor: String = ""

    init {
        this.id = movie.id
        this.name = movie.name
        this.price = movie.price
        this.stockQuantity = movie.stockQuantity
        this.director = movie.director
        this.actor = movie.actor
    }
}

class BookDto(book: Book) : ItemDto() {
    var author: String = ""
    var isbn: String = ""

    init {
        this.id = book.id
        this.name = book.name
        this.price = book.price
        this.stockQuantity = book.stockQuantity
        this.author = book.author
        this.isbn = book.author
    }
}


class ItemResponse(page: Page<ItemDto>) {
    var list: MutableList<ItemDto> = ArrayList()
    var page: PageResponse = PageResponse()

    init {
        this.list = page.content
        this.page.totalElements = page.totalElements
        this.page.last = page.isLast
        this.page.number = page.number
        this.page.totalPages = page.totalPages
    }
}