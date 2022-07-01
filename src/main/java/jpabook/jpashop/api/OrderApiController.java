package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> readOrdersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDTO> readOrdersV2() {
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDTO> readOrdersV3() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDTO> readOrdersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        return orderRepository.findAllWithMemberAndDelivery(offset, limit).stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    static class OrderDTO {
        private Long orderID;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        private List<OrderItemDTO> orderItems;

        public OrderDTO(Order order) {
            this.orderID = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getOrderStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream().map(OrderItemDTO::new).collect(Collectors.toList());
        }
    }

    @Data
    @AllArgsConstructor
    static class OrderItemDTO {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDTO(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
