package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        //when
        int orderCount = 2;
        Long resultID = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order result = orderRepository.findByID(resultID);

        assertThat(result.getOrderStatus()).as("상품 주문 시 상태는 ORDER 이어야 한다.").isEqualTo(OrderStatus.ORDER);
        assertThat(result.getOrderItems().size()).as("주문한 상품 종류 수가 일치해야 한다.").isEqualTo(1);
        assertThat(result.getTotalPrice()).as("주문 가격은 가격 * 수량이다.").isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).as("주문 수량만큼 재고가 줄어야 한다.").isEqualTo(8);
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderID = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderID);

        //then
        Order order = orderRepository.findByID(orderID);
        assertThat(order.getOrderStatus()).as("상품 주문 취소 시 상태는 CANCEL 이어야 한다.").isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).as("주문이 취소된 상품은 재고가 원상복구 되어야 한다.").isEqualTo(10);
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        //when
        int orderCount = 11;
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("AJW");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}