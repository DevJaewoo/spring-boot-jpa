package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> readOrdersV1() {
        return orderRepository.findAll();
    }

    @GetMapping("/api/v2/orders")
    public List<SimpleOrderDTO> readOrdersV2() {
        return orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<SimpleOrderDTO> readOrdersV3() {
        return orderRepository.findAllWithMemberAndDelivery().stream()
                .map(SimpleOrderDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    static class SimpleOrderDTO {
        private Long orderID;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDTO(Order order) {
            this.orderID = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();
        }
    }
}
