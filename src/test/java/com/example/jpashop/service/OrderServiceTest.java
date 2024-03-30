package com.example.jpashop.service;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Member;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.domain.item.Book;
import com.example.jpashop.domain.item.Item;
import com.example.jpashop.exception.NotEnoughStockException;
import com.example.jpashop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void order() throws Exception{
        Member member = new Member();
        member.setName("m1");
        member.setAddress(new Address("Seoul", "some", "123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);

        em.persist(book);

        int orderCnt = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        Order getOrder = orderRepository.findOne(orderId);
        System.out.println(book.getStockQuantity());

        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        Assertions.assertEquals(1, getOrder.getOrderItems().size());
        Assertions.assertEquals(10000 * orderCnt, getOrder.getTotalPrice());
        Assertions.assertEquals(8, book.getStockQuantity());
    }
    @Test
    public void cancel() throws Exception{
        Member member = new Member();
        member.setName("m1");
        member.setAddress(new Address("Seoul", "some", "123"));
        em.persist(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);

        em.persist(book);

        int cnt = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), cnt);
        orderService.cancelOrder(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        Assertions.assertEquals(10, book.getStockQuantity());
    }
    @Test
    public void overOrdered() throws Exception{
        try {
            Member member = new Member();
            member.setName("m1");
            member.setAddress(new Address("Seoul", "some", "123"));
            em.persist(member);


            Book book = new Book();
            book.setName("JPA");
            book.setPrice(10000);
            book.setStockQuantity(10);

            em.persist(book);
//            System.out.println("dbhsjcbdbhjcbsjhbcjhsdbcjhsbdcj" + book.get());

            int cnt = 11;

            orderService.order(member.getId(), book.getId(), cnt);
        } catch (NotEnoughStockException e){
//            e.printStackTrace();
            return;
        }
        fail("cannot not be here");
    }
}