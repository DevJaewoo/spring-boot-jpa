package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDTO> findOrderDTOList() {
        String jpql = "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDTO(o.id, m.name, o.orderDate, o.orderStatus, d.address) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d";

        return em.createQuery(jpql, OrderSimpleQueryDTO.class).getResultList();
    }
}
