package com.example.jpashop.api;

import com.example.jpashop.domain.Address;
import com.example.jpashop.domain.Order;
import com.example.jpashop.domain.OrderStatus;
import com.example.jpashop.repository.OrderRepository;
import com.example.jpashop.repository.OrderSearch;
import com.example.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOner, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 * 
 * 무한 로딩이 걸리기 때문에 양방향 연관관계중 하나는 JSONIgnore 해주기
 *
 * 레파지토리는 엔티티용으로, 심플 쿼리는 따로 패키지 만들기
 * 심플 쿼리로 하면 재사용성이 없음...
 * 
 * 1. 우선 엔티티를 DTO
 * 2. 필여하면 페치 조인
 * 3. 그래도 안되면 DTO로 직접 조회
 * 4. 최후는 네이티브 SQL이나 스프링 JDBC로 SQL 작성
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

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        List<SimpleOrderDto> result = orderRepository.findAllByCriteria((new OrderSearch())).stream().map(o -> new SimpleOrderDto(o)).collect((Collectors.toList()));
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream().map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderRepository.findorderDto();
    }


    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName(); //lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //lazy 초기화
        }
    }
}
