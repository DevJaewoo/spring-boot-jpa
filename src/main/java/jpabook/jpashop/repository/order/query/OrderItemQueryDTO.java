package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemQueryDTO {
    @JsonIgnore
    private Long orderID;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDTO(Long orderID, String itemName, int orderPrice, int count) {
        this.orderID = orderID;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
