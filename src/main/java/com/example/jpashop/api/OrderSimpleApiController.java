package com.example.jpashop.api;

import com.example.jpashop.domain.Order;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOner, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 * 
 * 무한 로딩이 걸리기 때문에 양방향 연관관계중 하나는 JSONIgnore 해주기
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order : all){
            order.getMember().getName();// Lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return all;
    }
}
