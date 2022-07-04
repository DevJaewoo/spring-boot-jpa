package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDTO> findOrderQueryDTOs() {
        List<OrderQueryDTO> result = findOrders();
        result.forEach(orderQueryDTO -> {
            List<OrderItemQueryDTO> orderItems = findOrderItems(orderQueryDTO.getId());
            orderQueryDTO.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDTO> findOrderItems(Long orderID) {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :order_id";

        return em.createQuery(jpql, OrderItemQueryDTO.class)
                .setParameter("order_id", orderID)
                .getResultList();
    }

    private List<OrderQueryDTO> findOrders() {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderQueryDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address) from Order o" +
                " join o.member m" +
                " join o.delivery d";

        return em.createQuery(jpql, OrderQueryDTO.class).getResultList();
    }

    public List<OrderQueryDTO> findAllByDTO_optimization() {
        List<OrderQueryDTO> result = findOrders();

        List<Long> orderIDs = result.stream().map(OrderQueryDTO::getId).collect(Collectors.toList());

        Map<Long, List<OrderItemQueryDTO>> orderItemMap = findOrderItemMap(orderIDs);
        result.forEach(orderQueryDTO -> orderQueryDTO.setOrderItems(orderItemMap.get(orderQueryDTO.getId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDTO>> findOrderItemMap(List<Long> orderIDs) {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderItemQueryDTO(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :order_ids";

        List<OrderItemQueryDTO> orderItems = em.createQuery(jpql, OrderItemQueryDTO.class)
                .setParameter("order_ids", orderIDs)
                .getResultList();

        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDTO::getOrderID));
    }

    public List<OrderFlatDTO> findAllByDTO_flat() {
        String jpql = "select new jpabook.jpashop.repository.order.query.OrderFlatDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count) from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i";

        return em.createQuery(jpql, OrderFlatDTO.class).getResultList();
    }
}
