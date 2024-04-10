package com.example.jpashop;

import com.example.jpashop.domain.*;
import com.example.jpashop.domain.item.Book;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final InitService initService;
    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }
    @RequiredArgsConstructor
    @Transactional
    @Component
    static class InitService{
        private final EntityManager em;
        public void dbInit1(){
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "1", "111"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA BOOK1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("JPA BOOK2");
            book2.setPrice(20000);
            book2.setStockQuantity(200);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 =OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createorder(member, delivery, orderItem, orderItem2);
            em.persist(order);
        }
        public void dbInit2(){
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("진주", "2", "222"));
            em.persist(member);

            Book book = new Book();
            book.setName("SPRING BOOK1");
            book.setPrice(20000);
            book.setStockQuantity(200);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("SPRING BOOK2");
            book2.setPrice(40000);
            book2.setStockQuantity(300);
            em.persist(book2);

            OrderItem orderItem = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem orderItem2 =OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createorder(member, delivery, orderItem, orderItem2);
            em.persist(order);
        }

    }
}
