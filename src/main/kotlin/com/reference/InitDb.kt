package com.reference

import com.reference.model.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager

@Component
class InitDb {
    @Autowired
    private lateinit var initService: InitService

    @PostConstruct
    fun init() {
        initService.dbInit1()
        initService.dbInit2()
    }

    @Component
    @Transactional
    internal class InitService {
        @Autowired
        private lateinit var em: EntityManager
        fun dbInit1() {
            val member: Member = createMember("rkdals213@naver.com", "1234", "강민형", "경기도 광명시", "철산로 57", "1304-205")
            em.persist(member)

            val category0: Category = createCategory1("ITEM")
            em.persist(category0)
            val category1: Category = createCategory1("BOOK")
            em.persist(category1)
            val category2: Category = createCategory1("ALBUM")
            em.persist(category2)
            val category3: Category = createCategory1("MOVIE")
            em.persist(category3)
            category0.addChildCategory(category1)
            category0.addChildCategory(category2)
            category0.addChildCategory(category3)
            em.persist(category0)

            val book1: Book = createBook("JPA1 BOOK", 10000, 100, "김영한1", "인프런", category1)
            em.persist(book1)
            val book2: Book = createBook("JPA2 BOOK", 20000, 100, "김영한2", "인프런", category1)
            em.persist(book2)
            val book3: Book = createBook("JPA3 BOOK", 30000, 100, "김영한3", "인프런", category1)
            em.persist(book3)
            val book4: Book = createBook("SPRING1 BOOK", 20000, 200, "김영한4", "인프런", category1)
            em.persist(book4)
            val book5: Book = createBook("SPRING2 BOOK", 40000, 300, "김영한5", "인프런", category1)
            em.persist(book5)
            category1.items.add(book1)
            category1.items.add(book2)
            category1.items.add(book3)
            category1.items.add(book4)
            category1.items.add(book5)
            em.persist(category1)

            val album1: Album = createAlbum("JAURIM", 14000, 100, "자우림", "ㅈㅇㄹ", category2)
            em.persist(album1)
            val album2: Album = createAlbum("YUNHA", 13000, 110, "윤하", "ㅇㅎ", category2)
            em.persist(album2)
            val album3: Album = createAlbum("DYNAMIC DUO", 12000, 90, "다이나믹 듀오", "ㄷㅇㄴㅁ ㄷㅇ", category2)
            em.persist(album3)
            val album4: Album = createAlbum("EPIK HIGH", 11000, 100, "에픽하이", "ㄷㅍㅎㅇ", category2)
            em.persist(album4)
            category2.items.add(album1)
            category2.items.add(album2)
            category2.items.add(album3)
            category2.items.add(album4)
            em.persist(category2)

            val movie1: Movie = createMovie("어벤져스", 14000, 100, "뭔가 유명한 사람", "유명한 감독", category3)
            em.persist(movie1)
            val movie2: Movie = createMovie("스파이더 맨", 13000, 110, "거미", "유명한 감독", category3)
            em.persist(movie2)
            val movie3: Movie = createMovie("캡틴 아메리카", 12000, 90, "트럼프", "바이든", category3)
            em.persist(movie3)
            val movie4: Movie = createMovie("나홀로 집에", 11000, 100, "케빈", "도둑", category3)
            em.persist(movie4)
            category3.items.add(movie1)
            category3.items.add(movie2)
            category3.items.add(movie3)
            category3.items.add(movie4)
            em.persist(category3)


//            val orderItem1: OrderItem = OrderItem.createOrderItem(book1, 10000, 1)
//            val orderItem2: OrderItem = OrderItem.createOrderItem(book2, 10000, 2)
//            val orderItem3: OrderItem = OrderItem.createOrderItem(book3, 30000, 2)
//            val order: Order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2, orderItem3)
//            em.persist(order)
//            val delivery: Delivery = createDelivery(member)
//            val orderItem4: OrderItem = OrderItem.createOrderItem(book1, 20000, 3)
//            val orderItem5: OrderItem = OrderItem.createOrderItem(book2, 40000, 4)
//            val order2: Order = Order.createOrder(member, delivery, orderItem4, orderItem5)
//            em.persist(order2)

        }

        fun dbInit2() {
        }

        private fun createMember(
            email: String,
            password: String,
            name: String,
            city: String,
            street: String,
            zipcode: String
        ): Member {
            val member = Member()
            member.email = email
            member.password = password
            member.name = name
            member.address = Address(city, street, zipcode)
            return member
        }

        private fun createBook(name: String, price: Int, stockQuantity: Int, author: String, isbn: String, category: Category): Book {
            val book = Book()
            book.name = name
            book.price = price
            book.stockQuantity = stockQuantity
            book.author = author
            book.isbn = isbn
            book.categories.add(category)
            return book
        }

        private fun createAlbum(name: String, price: Int, stockQuantity: Int, artist: String, etc: String, category: Category): Album {
            val album = Album()
            album.name = name
            album.price = price
            album.stockQuantity = stockQuantity
            album.artist = artist
            album.etc = etc
            album.categories.add(category)
            return album
        }

        private fun createMovie(name: String, price: Int, stockQuantity: Int, actor: String, director: String, category: Category): Movie {
            val movie = Movie()
            movie.name = name
            movie.price = price
            movie.stockQuantity = stockQuantity
            movie.actor = actor
            movie.director = director
            movie.categories.add(category)
            return movie
        }

        private fun createDelivery(member: Member): Delivery {
            val delivery = Delivery()
            delivery.address = member.address
            return delivery
        }

        private fun createCategory2(name: String, parent: Category): Category {
            val category = Category()
            category.name = name
            category.parent = parent
            return category
        }

        private fun createCategory1(name: String): Category {
            val category = Category()
            category.name = name
            return category
        }

    }
}
