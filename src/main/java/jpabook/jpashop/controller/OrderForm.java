package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OrderForm {
    @NotNull private Long memberID;
    @NotNull private Long itemID;
    @NotNull private int count;
}
